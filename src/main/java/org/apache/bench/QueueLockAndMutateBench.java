package org.apache.bench;

import org.apache.curator.framework.CuratorFramework;

import java.util.List;

public class QueueLockAndMutateBench extends AbstractBenchmark {
  public static final String NAME = "queueLockMutate";

  public QueueLockAndMutateBench(final CmdArgs config) {
    super(config);
  }

  @Override
  protected void setup(CuratorFramework client) throws Exception {

  }

  @Override
  protected void teardown(CuratorFramework client) throws Exception {

  }

  @Override
  public Task createTask(CmdArgs cmdArgs) {
    return null;
  }

  @Override
  public SuccessResult aggregateMetrics(List<SuccessResult> metrics) {
    return null;
  }

  public static class Factory implements Benchmark.Factory<QueueLockAndMutateBench> {

    @Override
    public QueueLockAndMutateBench create(CmdArgs config) {
      return new QueueLockAndMutateBench(config);
    }
  }
}
