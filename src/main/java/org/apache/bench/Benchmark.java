package org.apache.bench;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();
      metrics.forEach((k, v) -> sb.append(k).append("\n").append(v).append("\n"));
      return sb.toString();
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
    private String taskId;

    private long numSuccesses;
    private long numFailure;
    private long maxSuccessRequestTimeInNanoSec = Long.MIN_VALUE;
    private long minSuccessRequestTimeInNanoSec = Long.MAX_VALUE;
    private long totalTimeForAllTasksNanoSec;

    TaskStatistics(String name, int taskId) {
      this.name = name;
      this.taskId = Integer.toString(taskId);
    }

    /**
     * Usually only used by aggregated results across tasks
     * @param name
     */
    TaskStatistics(String name) {
      this.name = name;
      this.taskId = "";
    }

    public String getName() {
      return name;
    }

    public void addSuccess(long requestTime) {
      ++numSuccesses;
      updateTimes(requestTime);
    }

    public double getSuccess() {
      return numSuccesses;
    }

    public void addFailure(long requestTime) {
      ++numFailure;
      totalTimeForAllTasksNanoSec += requestTime;
    }

    public double getCurrentThroughput() {
      return (numSuccesses / totalTimeForAllTasksNanoSec);
    }

    private void updateTimes(long newTime) {
      maxSuccessRequestTimeInNanoSec = Math.max(maxSuccessRequestTimeInNanoSec, newTime);
      minSuccessRequestTimeInNanoSec = Math.min(minSuccessRequestTimeInNanoSec, newTime);
      totalTimeForAllTasksNanoSec += newTime;
    }

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("================================================================\n");
      sb.append(String.format("Final result for task type: %s and id: %s \n", name, taskId));
      sb.append(String.format("Success, Failure, Total_Across_Resources(ms), MaxRequestTime(ms), MinRequestTime(ms), " +
          "Average Time(ms), IOPS\n"));
      sb.append(String.format("%s, %s, %s, %s, %s, %s, %s\n", numSuccesses, numFailure ,
        TimeUnit.MILLISECONDS.convert(totalTimeForAllTasksNanoSec, TimeUnit.NANOSECONDS),
        TimeUnit.MILLISECONDS.convert(maxSuccessRequestTimeInNanoSec, TimeUnit.NANOSECONDS),
        TimeUnit.MILLISECONDS.convert(minSuccessRequestTimeInNanoSec, TimeUnit.NANOSECONDS),
        TimeUnit.MILLISECONDS.convert((totalTimeForAllTasksNanoSec / (numSuccesses == 0 ? 1 : numSuccesses)), TimeUnit.NANOSECONDS),
        (numSuccesses  * 1000000000 / (totalTimeForAllTasksNanoSec == 0 ? 1 : totalTimeForAllTasksNanoSec))));
      sb.append("================================================================\n");
      return sb.toString();
    }

    public TaskStatistics addOtherStats(TaskStatistics other) {
      this.taskId = this.taskId.concat(String.format(" -- %s", other.taskId));
      this.numSuccesses += other.numSuccesses;
      this.numFailure += other.numFailure;
      this.maxSuccessRequestTimeInNanoSec = Math.max(this.maxSuccessRequestTimeInNanoSec, other.maxSuccessRequestTimeInNanoSec);
      this.minSuccessRequestTimeInNanoSec = Math.min(this.minSuccessRequestTimeInNanoSec, other.minSuccessRequestTimeInNanoSec);
      // Take the max out of all the stats since all tasks were executed in parallel
      this.totalTimeForAllTasksNanoSec = Math.max(this.totalTimeForAllTasksNanoSec, other.totalTimeForAllTasksNanoSec);
      return this;
    }
  }
}
