package org.apache.bench;

import com.google.common.base.Preconditions;

import java.util.Map;

public class LockAndMutateBench implements Benchmark {
  public static final String NAME = "lockMutate";

  private final Map<String, String> config;

  private LockAndMutateBench(final Map<String, String> config) {
    this.config = Preconditions.checkNotNull(config);
  }

  @Override
  public void run() {
  }

  public static class Factory implements Benchmark.Factory<LockAndMutateBench> {
    @Override
    public LockAndMutateBench create(final Map<String, String> config) {
      return new LockAndMutateBench(config);
    }
  }
}
