package org.apache.bench;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LockAndMutateBench extends AbstractBenchmark {
  public static final String NUM_LOCK_ACQUIRES = "Number of lock acquires";
  public static final String TIME_TO_ACQUIRE = "Time to acquire";
  public static final String TIME_TO_RELEASE = "Time to release";

  public static final String AVERAGE_TIME_TO_ACQUIRE = "Average time to acquire millis.";
  public static final String AVERAGE_TIME_TO_RELEASE = "Average time to release millis.";

  public static final String NAME = "lockMutate";

  public static final String BASE_PATH = "org/apache/zookeeperbench/" + LockAndMutateBench.NAME;
  public static final String LOCK_PATH = BASE_PATH + "/lock";

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
    Map<String, Double> aggregateMetrics = new HashMap<>();

    double numLockAcquires  = 0;
    double totalTimeToAcquire = 0;
    double totalTimeToRelease = 0;

    for (SuccessResult result: results) {
      numLockAcquires += result.getMetrics().get(NUM_LOCK_ACQUIRES);
      totalTimeToAcquire += result.getMetrics().get(TIME_TO_ACQUIRE);
      totalTimeToRelease += result.getMetrics().get(TIME_TO_RELEASE);
    }

    double averageTimeToAcquire = totalTimeToAcquire / numLockAcquires;
    double averageTimeToRelease = totalTimeToRelease / numLockAcquires;

    aggregateMetrics.put(AVERAGE_TIME_TO_ACQUIRE, averageTimeToAcquire);
    aggregateMetrics.put(AVERAGE_TIME_TO_RELEASE, averageTimeToRelease);

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
    public Task(CmdArgs cmdArgs) {
      super(cmdArgs);
    }

    @Override
    public Result runTask(CuratorFramework client) {
      double numAcquires = 0;
      Stopwatch timeToAcquire = new Stopwatch();
      Stopwatch timeToRelease = new Stopwatch();
      final Map<String, Double> metrics = new HashMap<>();

      while (!isTerminated()) {
        InterProcessMutex mutex = new InterProcessMutex(client, LOCK_PATH);

        try {
          timeToAcquire.start();
          mutex.acquire(5, TimeUnit.MINUTES);
          timeToAcquire.start();
          numAcquires++;
        } catch (Exception e) {
          return new FailureResult("Timedout acquiring lock.", Lists.newArrayList(e));
        }

        try {
          timeToRelease.start();
          mutex.release();
          timeToRelease.stop();
        } catch (Exception e) {
          return new FailureResult("Failed releasing lock.", Lists.newArrayList(e));
        }
      }

      metrics.put(NUM_LOCK_ACQUIRES, numAcquires);
      metrics.put(TIME_TO_ACQUIRE, (double) timeToAcquire.elapsed(TimeUnit.MILLISECONDS));
      metrics.put(TIME_TO_RELEASE, (double) timeToRelease.elapsed(TimeUnit.MILLISECONDS));

      return new SuccessResult(metrics);
    }
  }
}
