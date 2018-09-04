package org.apache.bench;

public class QueueBench extends AbstractBenchmark {
  public static final String NAME = "queue";

  public QueueBench(final CmdArgs config) {
    super(config);
  }

  @Override
  public void run() {

  }

  public static class Factory implements Benchmark.Factory<QueueBench> {

    @Override
    public QueueBench create(CmdArgs config) {
      return new QueueBench(config);
    }
  }
}
