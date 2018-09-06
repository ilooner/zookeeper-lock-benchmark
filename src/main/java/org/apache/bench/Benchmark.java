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
    Result runTask(CuratorFramework client);

    void terminate();

    void throttleIfRequired(long currentThroughput);
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
    private final Map<String, TaskStatistics> metrics;

    public SuccessResult(final Map<String, TaskStatistics> metrics) {
      this.metrics = Preconditions.checkNotNull(metrics);
    }

    @Override
    public ResultType getType() {
      return ResultType.SUCCESS;
    }

    public Map<String, TaskStatistics> getMetrics() {
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

  /**
   * Captures various metrics related to a benchmark tasks like:
   * - Count of successful tasks
   * - Count of failure tasks
   * - Average / Min / Max Task time
   * - IOPS of the tasks
   */
  class TaskStatistics {
    private final String name;

    private double numSuccesses;
    private double numFailure;
    private double maxSuccessRequestTimeInMs = Double.MIN_VALUE;
    private double minSuccessRequestTimeInMs = Double.MAX_VALUE;
    private double totalTimeForAllTasks;

    TaskStatistics(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void addSuccess(double requestTime) {
      ++numSuccesses;
      updateTimes(requestTime);
    }

    public void addFailure() {
      ++numFailure;
    }

    public double getCurrentThroughput() {
      return (numSuccesses / totalTimeForAllTasks);
    }

    private void updateTimes(double newTime) {
      maxSuccessRequestTimeInMs = Math.max(maxSuccessRequestTimeInMs, newTime);
      minSuccessRequestTimeInMs = Math.min(minSuccessRequestTimeInMs, newTime);
      totalTimeForAllTasks += newTime;
    }

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("================================================================\n");
      sb.append(String.format("Final result for task type: %s\n", name));
      sb.append(String.format("Count: Success: %s, Failure: %s\n", numSuccesses, numFailure));
      sb.append(String.format("Time: Total across resources(ms): %s, MaxRequestTime(ms): %s, " +
        "MinRequestTime(ms): %s,  Average Time(ms): %s\n", totalTimeForAllTasks, maxSuccessRequestTimeInMs,
        minSuccessRequestTimeInMs, (totalTimeForAllTasks / numSuccesses)));
      sb.append(String.format("IOPS: %s\n", (numSuccesses * 1000 / totalTimeForAllTasks)));
      sb.append("================================================================\n");
      return sb.toString();
    }

    public TaskStatistics addOtherStats(TaskStatistics other) {
      this.numSuccesses += other.numSuccesses;
      this.numFailure += other.numFailure;
      this.maxSuccessRequestTimeInMs = Math.max(this.maxSuccessRequestTimeInMs, other.maxSuccessRequestTimeInMs);
      this.minSuccessRequestTimeInMs = Math.min(this.minSuccessRequestTimeInMs, other.minSuccessRequestTimeInMs);
      this.totalTimeForAllTasks += other.totalTimeForAllTasks;
      return this;
    }
  }
}
