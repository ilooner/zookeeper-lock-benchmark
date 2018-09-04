package org.apache.bench;

import com.google.common.base.Preconditions;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractBenchmark implements Benchmark {
  private final CmdArgs config;

  public AbstractBenchmark(final CmdArgs config) {
    this.config = Preconditions.checkNotNull(config);
  }

  @Override
  public void run() {
    final ExecutorService executorService = Executors.newFixedThreadPool(config.numClients);

    executorService.shutdown();
  }

  public static CuratorFramework createClient(final CmdArgs cmdArgs) {
    final RetryPolicy policy = new RetryNTimes(3, 10000);
    final CuratorFramework client = CuratorFrameworkFactory.newClient(cmdArgs.getConnectionString(), policy);
    return client;
  }

  public static abstract class AbstractTask implements Task {
    private final CmdArgs cmdArgs;

    public AbstractTask(final CmdArgs cmdArgs) {
      this.cmdArgs = Preconditions.checkNotNull(cmdArgs);
    }

    @Override
    public Result call() throws Exception {
      CuratorFramework client = null;
      List<Exception> exceptionList = new ArrayList<>();
      Map<String, Long> metrics = null;

      try {
        client = createClient(cmdArgs);
        client.start();
        metrics = runTask(client);
      } catch (Exception ex) {
        exceptionList.add(ex);
      } finally {
        if (client != null) {
          try {
            client.close();
          } catch (Exception exClose) {
            exceptionList.add(exClose);
          }
        }
      }

      if (!exceptionList.isEmpty() || metrics == null) {
        return new FailureResult(exceptionList);
      } else {
        return new SuccessResult(metrics);
      }
    }

    public abstract Map<String, Long> runTask(CuratorFramework client);
  }
}
