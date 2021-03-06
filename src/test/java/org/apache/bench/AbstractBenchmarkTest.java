package org.apache.bench;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.apache.bench.AbstractBenchmarkTest.MockBenchmark.ITEM;

public class AbstractBenchmarkTest {
  public static final String CONNECTION_STRING = "127.0.0.1:2181";

  private TestingServer server;

  @Before
  public void setup() throws Exception {
    server = new TestingServer(2181);
    server.start();
  }

  @Test
  public void simpleSuccessTest() {
    CmdArgs cmdArgs = new CmdArgs();

    cmdArgs.connectionString = CONNECTION_STRING;
    cmdArgs.numClients = 4;
    cmdArgs.duration = "PT0.1S";

    Benchmark.SuccessResult result = (Benchmark.SuccessResult) new MockBenchmark(cmdArgs).run();

    Assert.assertEquals(1, result.getMetrics().size());
    final Benchmark.TaskStatistics taskStatistics = result.getMetrics().get(ITEM);
    Assert.assertEquals(4.0, taskStatistics.getSuccess(), .001);
  }

  @After
  public void tearDown() throws Exception {
    server.close();
  }

  public static class MockBenchmark extends AbstractBenchmark {
    public static final String ITEM = "item";

    public MockBenchmark(final CmdArgs cmdArgs) {
      super(cmdArgs);
    }

    @Override
    protected void setup(CuratorFramework client, InterProcessMutex globalInterProcessMutex) throws Exception {
    }

    @Override
    protected void teardown(CuratorFramework client, InterProcessMutex globalInterProcessMutex) throws Exception {

    }

    @Override
    protected Task createTask(CmdArgs cmdArgs, int taskId, CuratorFramework client) {
      return new MockTask(cmdArgs, 1, client);
    }

    public static class MockTask extends AbstractTask {
      public MockTask(CmdArgs cmdArgs, int taskId, CuratorFramework client) {
        super(cmdArgs, 1, client);
      }

      @Override
      public Result runTask(CuratorFramework client) {
        Map<String, TaskStatistics> stats = new HashMap<>();
        final TaskStatistics stat = new TaskStatistics(ITEM);
        stat.addSuccess(10);
        stats.put(ITEM, stat);

        return new SuccessResult(stats);
      }
    }
  }
}
