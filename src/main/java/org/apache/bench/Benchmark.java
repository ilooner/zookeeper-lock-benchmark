package org.apache.bench;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

public interface Benchmark {
  Result run();

  interface Task extends Callable<Result> {
    SuccessResult runTask(CuratorFramework client);

    void terminate();
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
    private final Map<String, Double> metrics;

    public SuccessResult(final Map<String, Double> metrics) {
      this.metrics = Preconditions.checkNotNull(metrics);
    }

    @Override
    public ResultType getType() {
      return ResultType.SUCCESS;
    }

    public Map<String, Double> getMetrics() {
      return this.metrics;
    }
  }

  class FailureResult implements Result {
    private final List<Exception> exceptions;
    private final String message;

    public FailureResult(final String message, final List<Exception> exceptions) {
      this.exceptions = Preconditions.checkNotNull(exceptions);
      this.message = message;
    }

    public FailureResult(final List<Exception> exceptions) {
      this.exceptions = Preconditions.checkNotNull(exceptions);
      this.message = null;
    }

    public FailureResult(final String message) {
      this.exceptions = Lists.newArrayList();
      this.message = Preconditions.checkNotNull(message);
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

    public String getMessage() {
      return message;
    }

    public static FailureResult aggregate(List<FailureResult> results) {
      final StringBuilder sb = new StringBuilder();
      final List<Exception> exceptions = Lists.newArrayList();

      results.forEach(result -> {
        Optional.ofNullable(result.getMessage()).ifPresent(
          message -> sb.append(message).append("\n"));

        exceptions.addAll(result.getExceptions());
      });

      String message = sb.toString();

      if (message.isEmpty()) {
        message = null;
      }

      return new FailureResult(message, exceptions);
    }
  }
}
