package org.apache.bench;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public interface Benchmark {
  void run();

  interface Task extends Callable<Result> {
    Map<String, Long> runTask(CuratorFramework client);
  }

  interface Factory<T extends Benchmark> {
    T create(CmdArgs config);
  }

  enum ResultType {
    SUCCESS,
    FAILURE
  }

  interface Result {
    ResultType getType();
  }

  class SuccessResult implements Result {
    private final Map<String, Long> metrics;

    public SuccessResult(final Map<String, Long> metrics) {
      this.metrics = Preconditions.checkNotNull(metrics);
    }

    @Override
    public ResultType getType() {
      return ResultType.SUCCESS;
    }

    public Map<String, Long> getMetrics() {
      return this.metrics;
    }
  }

  class FailureResult implements Result {
    private final List<Exception> exceptions;

    public FailureResult(final List<Exception> exceptions) {
      this.exceptions = Preconditions.checkNotNull(exceptions);
    }

    public FailureResult(final Exception... exceptions) {
      this(Lists.newArrayList(exceptions));
    }

    @Override
    public ResultType getType() {
      return ResultType.FAILURE;
    }

    public List<Exception> getExceptions() {
      return exceptions;
    }
  }
}
