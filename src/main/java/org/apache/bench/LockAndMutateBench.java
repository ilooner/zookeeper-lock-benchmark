package org.apache.bench;

import com.google.common.base.Stopwatch;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LockAndMutateBench extends AbstractBenchmark {
  public static final String ACQUIRE_TASK_NAME = "LockAcquireTasks";
  public static final String RELEASE_TASK_NAME = "LockReleaseTasks";
  public static final String TRANSACTION_TASK_NAME = "TransactionTasks";
  public static final TaskStatistics lockAcquireStats = new TaskStatistics(ACQUIRE_TASK_NAME);
  public static final TaskStatistics lockReleaseStats = new TaskStatistics(RELEASE_TASK_NAME);
  public static final TaskStatistics transactionStats = new TaskStatistics(TRANSACTION_TASK_NAME);
  public static final String NAME = "lockMutate";

  public static final String BASE_PATH = "/org/apache/zookeeperbench/" + LockAndMutateBench.NAME;
  public static final String LOCK_PATH = BASE_PATH + "/lock";
  public static final String BLOB_PATH_1 = BASE_PATH + "/blob1";
  public static final String BLOB_PATH_2 = BASE_PATH + "/blob2";

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
  public SuccessResult aggregateMetrics(List<SuccessResult> results) {
    Map<String, TaskStatistics> aggregateMetrics = new HashMap<>();

    TaskStatistics aggAcquireLockStats = new TaskStatistics(String.format("Aggregated-%s", ACQUIRE_TASK_NAME));
    TaskStatistics aggReleaseLockStats = new TaskStatistics(String.format("Aggregated-%s", RELEASE_TASK_NAME));
    TaskStatistics aggTransactionStats = new TaskStatistics(String.format("Aggregated-%s", TRANSACTION_TASK_NAME));

    aggAcquireLockStats = results.stream()
      .map(result -> result.getMetrics().get(ACQUIRE_TASK_NAME))
      .reduce(aggAcquireLockStats, TaskStatistics::addOtherStats);

    aggReleaseLockStats = results.stream()
      .map(result -> result.getMetrics().get(RELEASE_TASK_NAME))
      .reduce(aggReleaseLockStats, TaskStatistics::addOtherStats);

    aggTransactionStats = results.stream()
      .map(result -> result.getMetrics().get(TRANSACTION_TASK_NAME))
      .reduce(aggTransactionStats, TaskStatistics::addOtherStats);

    aggregateMetrics.put(aggAcquireLockStats.getName(), aggAcquireLockStats);
    aggregateMetrics.put(aggReleaseLockStats.getName(), aggReleaseLockStats);
    aggregateMetrics.put(aggTransactionStats.getName(), aggTransactionStats);

    return new SuccessResult(aggregateMetrics);
  }

  @Override
  public Task createTask(CmdArgs cmdArgs) {
    return new Task(cmdArgs);
  }

  public static class Factory implements Benchmark.Factory<LockAndMutateBench> {
    @Override
    public LockAndMutateBench create(final CmdArgs config) {
      return new LockAndMutateBench(config);
    }
  }

  public static class Task extends AbstractTask {

    enum TaskType {
      ACQUIRE,
      RELEASE,
      TRANSACTION
    }

    public Task(CmdArgs cmdArgs) {
      super(cmdArgs);
    }

    @Override
    public Result runTask(CuratorFramework client) {
      Stopwatch timeToAcquire = new Stopwatch();
      Stopwatch timeToRelease = new Stopwatch();
      Stopwatch timeToTransact = new Stopwatch();

      final Map<String, TaskStatistics> metrics = new HashMap<>();

      while (!isTerminated()) {
        InterProcessMutex mutex = new InterProcessMutex(client, LOCK_PATH);

        performTask(TaskType.ACQUIRE, timeToAcquire, lockAcquireStats, mutex, client);
        performTask(TaskType.TRANSACTION, timeToTransact, transactionStats, mutex, client);
        performTask(TaskType.RELEASE, timeToRelease, lockReleaseStats, mutex, client);

        final long totalThroughput = (long) (lockAcquireStats.getCurrentThroughput() +
          lockReleaseStats.getCurrentThroughput() + transactionStats.getCurrentThroughput());
        throttleIfRequired(totalThroughput);
      }

      metrics.put(lockAcquireStats.getName(), lockAcquireStats);
      metrics.put(lockReleaseStats.getName(), lockReleaseStats);
      metrics.put(transactionStats.getName(), transactionStats);

      return new SuccessResult(metrics);
    }

    private void performTask(TaskType taskType, Stopwatch taskTimer, TaskStatistics statistics,
                             InterProcessMutex mutex, CuratorFramework client) {
      try {
        taskTimer.start();
        if (taskType.equals(TaskType.ACQUIRE)) {
          mutex.acquire(5, TimeUnit.MINUTES);
        } else if (taskType.equals(TaskType.RELEASE)) {
          mutex.release();
        } else if (taskType.equals(TaskType.TRANSACTION)) {
          Collection<CuratorTransactionResult> results = client.inTransaction()
            .setData().forPath(BLOB_PATH_1, "test1".getBytes())
            .and()
            .setData().forPath(BLOB_PATH_2, "test2".getBytes())
            .and()
            .commit();
        }
        taskTimer.stop();
        statistics.addSuccess(taskTimer.elapsed(TimeUnit.MILLISECONDS));
      } catch (Exception e) {
        statistics.addFailure();
        //return new FailureResult("Failed releasing lock.", Lists.newArrayList(e));
      } finally {
        taskTimer.reset();
      }
    }
  }
}
