package org.apache.bench;

import java.util.List;
import java.util.Map;

public class QueueBench extends AbstractBenchmark {
  public static final String NAME = "queue";

  public QueueBench(final CmdArgs config) {
    super(config);
  }

  @Override
  public Map<String, Double> aggregateMetrics(List<Map<String, Double>> metrics) {
    return null;
  }

  @Override
  public Task createTask(CmdArgs cmdArgs) {
    return null;
  }

  public static class Factory implements Benchmark.Factory<QueueBench> {

    @Override
    public QueueBench create(CmdArgs config) {
      return new QueueBench(config);
    }
  }
}
