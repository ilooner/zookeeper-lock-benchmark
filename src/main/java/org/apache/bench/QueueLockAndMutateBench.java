package org.apache.bench;

public class QueueLockAndMutateBench extends AbstractBenchmark {
  public static final String NAME = "queueLockMutate";

  public QueueLockAndMutateBench(final CmdArgs config) {
    super(config);
  }

  @Override
  public void run() {

  }

  public static class Factory implements Benchmark.Factory<QueueLockAndMutateBench> {

    @Override
    public QueueLockAndMutateBench create(CmdArgs config) {
      return new QueueLockAndMutateBench(config);
    }
  }
}
