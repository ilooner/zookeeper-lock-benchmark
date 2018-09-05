package org.apache.bench;

import java.util.List;
import java.util.Map;

public class QueueLockAndMutateBench extends AbstractBenchmark {
  public static final String NAME = "queueLockMutate";

  public QueueLockAndMutateBench(final CmdArgs config) {
    super(config);
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
