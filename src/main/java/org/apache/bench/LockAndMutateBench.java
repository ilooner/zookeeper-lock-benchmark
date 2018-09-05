package org.apache.bench;

import org.apache.curator.framework.CuratorFramework;

import java.util.List;

public class LockAndMutateBench extends AbstractBenchmark {
  public static final String NAME = "lockMutate";

  private LockAndMutateBench(final CmdArgs config) {
    super(config);
  }

  @Override
  protected void setup(CuratorFramework client) throws Exception {

  }

  @Override
  protected void teardown(CuratorFramework client) throws Exception {

  }

  @Override
  public SuccessResult aggregateMetrics(List<SuccessResult> metrics) {
    return null;
  }

  @Override
  public Task createTask(CmdArgs cmdArgs) {
    return null;
  }

  public static class Factory implements Benchmark.Factory<LockAndMutateBench> {
    @Override
    public LockAndMutateBench create(final CmdArgs config) {
      return new LockAndMutateBench(config);
    }
  }
}
