package org.apache.bench;

import com.google.common.base.Stopwatch;
import org.apache.bench.ZKBlobDataGen.BlobData;
import org.apache.bench.ZKBlobDataGen.DataDefConstants;
import org.apache.bench.ZKBlobDataGen.NodeFreeUsedResourceData;
import org.apache.bench.ZKBlobDataGen.NodeQueryClusterUtilization;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LockAndMutateBench extends AbstractBenchmark {
  public static final String ACQUIRE_TASK_NAME = "LockAcquireTasks";
  public static final String RELEASE_TASK_NAME = "LockReleaseTasks";
  public static final String TRANSACTION_TASK_NAME = "TransactionTasks";
  public static final String NAME = "lockMutate";

  public static final String BASE_PATH = "/org/apache/zookeeperbench/" + LockAndMutateBench.NAME;
  public static final String LOCK_PATH = BASE_PATH + "/lock";
  public static final String BLOB_PATH_1 = BASE_PATH + "/blob1";
  public static final String BLOB_PATH_2 = BASE_PATH + "/blob2";

  public static byte[] blob1Data;
  public static byte[] blob2Data;

  private LockAndMutateBench(final CmdArgs config) {
    super(config);
  }

  @Override
  protected void setup(CuratorFramework client) throws Exception {
    int configNodeCount = config.getNodeCount();
    configNodeCount = (configNodeCount == 0) ? DataDefConstants.DEFAULT_NODE_COUNT : configNodeCount;

    // For blob1 the data is for leaf resource pool. So keeping the resource pool count to 2
    final BlobData blob1 = new NodeFreeUsedResourceData(configNodeCount);
    blob1.generate();
    blob1Data = blob1.getDataAsByteArray();

    // For blob2 the data is for cluster wide node resource pool
    final BlobData blob2 = new NodeQueryClusterUtilization(configNodeCount);
    blob2.generate();
    blob2Data = blob2.getDataAsByteArray();

    if (client.checkExists().forPath(BLOB_PATH_1) == null) {
      client.create().creatingParentsIfNeeded().forPath(BLOB_PATH_1, blob1Data);
    }

    if (client.checkExists().forPath(BLOB_PATH_2) == null) {
      client.create().creatingParentsIfNeeded().forPath(BLOB_PATH_2, blob2Data);
    }
  }

  @Override
  protected void teardown(CuratorFramework client) throws Exception {
    if (client.checkExists().forPath(BLOB_PATH_1) != null) {
      client.delete().forPath(BLOB_PATH_1);
    }

    if (client.checkExists().forPath(BLOB_PATH_2) != null) {
      client.delete().forPath(BLOB_PATH_2);
    }

    if (client.checkExists().forPath(LOCK_PATH) != null) {
      client.delete().forPath(LOCK_PATH);
    }
  }

  @Override
  protected void printBenchState() {
    System.out.println("Size of blob1 data: " + (blob1Data.length / 1024.0) + " KB");
    System.out.println("Size of blob2 data: " + (blob2Data.length / 1024.0) + " KB");
  }

  @Override
  public Task createTask(CmdArgs cmdArgs, int taskId) {
    return new Task(cmdArgs, taskId);
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

    private final Random r = new Random();

    public Task(CmdArgs cmdArgs, int taskId) {
      super(cmdArgs, taskId);
    }

    @Override
    public Result runTask(CuratorFramework client) {
      Stopwatch timeToAcquire = new Stopwatch();
      Stopwatch timeToRelease = new Stopwatch();
      Stopwatch timeToTransact = new Stopwatch();

      final TaskStatistics lockAcquireStats = new TaskStatistics(ACQUIRE_TASK_NAME, taskId);
      final TaskStatistics lockReleaseStats = new TaskStatistics(RELEASE_TASK_NAME, taskId);
      final TaskStatistics transactionStats = new TaskStatistics(TRANSACTION_TASK_NAME, taskId);

      final Map<String, TaskStatistics> metrics = new HashMap<>();

      while (!isTerminated()) {
        InterProcessMutex mutex = new InterProcessMutex(client, LOCK_PATH);

        performTask(TaskType.ACQUIRE, timeToAcquire, lockAcquireStats, mutex, client);
        if (!cmdArgs.isTransactionsDisabled()) {
          performTask(TaskType.TRANSACTION, timeToTransact, transactionStats, mutex, client);
        }
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
          // Currently just a stub
          client.getData().forPath(BLOB_PATH_1);
          client.getData().forPath(BLOB_PATH_2);

          // Choose a random time between min and max sleep time to mimic the time needed for transaction.
          // If both min and max sleep time is same then don't sleep at all
          if (cmdArgs.getMinSleepTimeInMs() != cmdArgs.getMaxSleepTimeInMs()) {
            int randomSleepTime = r.nextInt((cmdArgs.getMaxSleepTimeInMs() - cmdArgs.getMinSleepTimeInMs()) + 1)
              + cmdArgs.getMinSleepTimeInMs();
            Thread.sleep(randomSleepTime);
          }

          Collection<CuratorTransactionResult> results = client.inTransaction()
            .setData().forPath(BLOB_PATH_1, blob1Data)
            .and()
            .setData().forPath(BLOB_PATH_2, blob2Data)
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
