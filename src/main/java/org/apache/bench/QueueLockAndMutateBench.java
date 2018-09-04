package org.apache.bench;

import com.google.common.base.Preconditions;

import java.util.Map;

public class QueueLockAndMutateBench implements Benchmark {
  public static final String NAME = "queueLockMutate";

  private final Map<String, String> config;

  public QueueLockAndMutateBench(final Map<String, String> config) {
    this.config = Preconditions.checkNotNull(config);
  }

  @Override
  public void run() {

  }

  public static class Factory implements Benchmark.Factory<QueueLockAndMutateBench> {

    @Override
    public QueueLockAndMutateBench create(Map<String, String> config) {
      return new QueueLockAndMutateBench(config);
    }
  }
}
