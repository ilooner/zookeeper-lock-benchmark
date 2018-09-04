package org.apache.bench;

public class LockAndMutateBench extends AbstractBenchmark {
  public static final String NAME = "lockMutate";

  private LockAndMutateBench(final CmdArgs config) {
    super(config);
  }

  @Override
  public void run() {
  }

  public static class Factory implements Benchmark.Factory<LockAndMutateBench> {
    @Override
    public LockAndMutateBench create(final CmdArgs config) {
      return new LockAndMutateBench(config);
    }
  }
}
