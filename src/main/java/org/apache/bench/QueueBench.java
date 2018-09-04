package org.apache.bench;

import com.google.common.base.Preconditions;

import java.util.Map;

public class QueueBench implements Benchmark {
  public static final String NAME = "queue";

  private Map<String, String> config;

  public QueueBench(final Map<String, String> config) {
    this.config = Preconditions.checkNotNull(config);
  }

  @Override
  public void run() {

  }

  public static class Factory implements Benchmark.Factory<QueueBench> {

    @Override
    public QueueBench create(Map<String, String> config) {
      return new QueueBench(config);
    }
  }
}
