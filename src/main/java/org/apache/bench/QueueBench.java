package org.apache.bench;

import com.google.common.base.Stopwatch;
import org.apache.bench.queue.DistributedQueue;
import org.apache.bench.scheduler.Scheduler;
import org.apache.bench.queue.DistributedQueue.QueueLease;
import org.apache.bench.semaphore.DistributedSemaphore.DistributedLease;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class QueueBench extends AbstractBenchmark {
  public static final String NAME = "queue";
  public static final long CLUSTER_CPU = 10;
  public static final long CLUSTER_MEMORY = 100;
  public static final String ACQUIRE_TASK_NAME = "LockAcquireTasks";
  public static final String RELEASE_TASK_NAME = "LockReleaseTasks";
  public static final String TASK_TIMEOUT_NAME = "TimeoutTasks";

  private ResourceManager rm;

  public static class ResourceManager {
    private final Map<String, DistributedQueue> queues;
    private final Scheduler scheduler;
    private final Map<Integer, String> costToQueueMap;

    public ResourceManager(Map<String, DistributedQueue> queues,
                           Scheduler scheduler, Map<Integer, String> costMapping) {
      this.queues = queues;
      this.scheduler = scheduler;
      this.costToQueueMap = costMapping;
    }

    public QueueLease allocate(Query qry, long time, TimeUnit timeUnit) throws Exception {
      String queuename = this.costToQueueMap.get(qry.getQueryCost());
      return new QueueLease(queuename, scheduler.schedule(qry, this.queues.get(queuename), time, timeUnit));
    }

    public void release(QueueLease lease) throws Exception {
      this.queues.get(lease.queueName()).release(lease.getDistributedLease());
    }

    public void close(CuratorFramework client) throws Exception {
      for (Map.Entry<String, DistributedQueue> e : queues.entrySet()) {
        e.getValue().close(client);
      }
    }
  }

  public static class QueryService {
    public final int taskID;
    private int qNo;
    private final CmdArgs args;
    private final Random r = new Random();

    public QueryService(int tid, CmdArgs args) {
      this.taskID = tid;
      this.qNo = 0;
      this.args = args;
    }

    public Query buildQuery() {
      int qExecTime = r.nextInt((args.maxQueryExecTime - args.minQueryExecTime) + 1) + args.minQueryExecTime;
      int qCost = r.nextInt((args.maxQueryCost - args.minQueryCost) + 1) + args.minQueryCost;
      return new Query(Integer.toString(taskID) + Integer.toString(qNo++), qCost, qExecTime);
    }

    public void execute(Query query) {
      try {
        Thread.sleep(query.getQueryTime());
      } catch (InterruptedException ex) {

      }
    }
  }

  public static class Query {
    private String queryID;
    private long queryTime;
    private int queryCost;

    public Query(String qID, int queryCost, int queryExecTime) {
      this.queryID = qID;
      this.queryTime = queryExecTime;
      this.queryCost = queryCost;
    }

    public long getQueryTime() {
      return queryTime;
    }

    public String queryID() {
      return queryID;
    }

    public int getQueryCost() {
      return this.queryCost;
    }
  }

  public QueueBench(final CmdArgs config) {
    super(config);
  }

  @Override
  protected void setup(CuratorFramework client, InterProcessMutex globalInterProcessMutex) throws Exception {
    Map<String, DistributedQueue> queues = new HashMap<>();
    Map<Integer, String> costToQueueMap = new HashMap<>();
    double equalDist = 1/this.config.queueCount;

    globalInterProcessMutex.acquire();
    for (int i=0;i<this.config.queueCount;i++) {
      String qname = NAME + i;
      queues.put(qname, new DistributedQueue(qname, (long)(CLUSTER_CPU * equalDist),
              (long)(CLUSTER_MEMORY * equalDist), this.config.numOfLeases, BASE_PATH, client));
      costToQueueMap.put(i, qname);
    }

    this.rm = new ResourceManager(queues, new Scheduler() {
      @Override
      public DistributedLease schedule(Query qry, DistributedQueue queue, long time, TimeUnit timeUnit) throws Exception {
        DistributedLease lease = queue.enqueue(time, timeUnit);
        //The logic of allocating the queue resources for this query goes here.
        return lease;
      }
    }, costToQueueMap);
    globalInterProcessMutex.release();
  }

  public static class Factory implements Benchmark.Factory<QueueBench> {
    @Override
    public QueueBench create(final CmdArgs config) {
      return new QueueBench(config);
    }
  }

  @Override
  protected void teardown(CuratorFramework client, InterProcessMutex globalInterProcessMutex) throws Exception {
    globalInterProcessMutex.acquire();
    this.rm.close(client);
    globalInterProcessMutex.release();
  }

  @Override
  public Task createTask(CmdArgs cmdArgs, int taskId, CuratorFramework client) {
    return new Task(cmdArgs, this.rm, taskId, client);
  }

  public static class Task extends AbstractTask {

    private final ResourceManager rm;
    private final QueryService queryService;

    public Task(CmdArgs cmdArgs, ResourceManager rm, int taskId, CuratorFramework client) {
      super(cmdArgs, taskId, client);
      this.queryService = new QueryService(this.hashCode(), cmdArgs);
      this.rm = rm;
    }

    @Override
    public Result runTask(CuratorFramework client) {
      Stopwatch timer = new Stopwatch();
      final TaskStatistics lockAcquireStats = new TaskStatistics(ACQUIRE_TASK_NAME, this.taskId);
      final TaskStatistics lockReleaseStats = new TaskStatistics(RELEASE_TASK_NAME, this.taskId);
      final TaskStatistics lockTimeOutStats = new TaskStatistics(TASK_TIMEOUT_NAME, this.taskId);

      final Map<String, TaskStatistics> metrics = new HashMap<>();

      while (!isTerminated()) {
        Query currentQuery = queryService.buildQuery();
        QueueLease lease;
        try {
          timer.reset();
          timer.start();
          lease = rm.allocate(currentQuery, cmdArgs.queryWaitTime, TimeUnit.MILLISECONDS);
          timer.stop();
          if (lease.getDistributedLease() == null) {
            lockTimeOutStats.addFailure(timer.elapsed(TimeUnit.NANOSECONDS));
            continue;
          } else {
            lockAcquireStats.addSuccess(timer.elapsed(TimeUnit.NANOSECONDS));
          }
        } catch (Exception ex) {
          timer.stop();
          System.out.println(ex.getStackTrace());
          lockAcquireStats.addFailure(timer.elapsed(TimeUnit.NANOSECONDS));
          continue;
        }

        queryService.execute(currentQuery);

        try {
          timer.reset();
          timer.start();
          rm.release(lease);
          timer.stop();
          lockReleaseStats.addSuccess(timer.elapsed(TimeUnit.NANOSECONDS));
        } catch (Exception ex) {
          timer.stop();
          lockReleaseStats.addFailure(timer.elapsed(TimeUnit.NANOSECONDS));
        }

        final long totalThroughput = (long) (lockAcquireStats.getCurrentThroughput() +
                lockReleaseStats.getCurrentThroughput());
        throttleIfRequired(totalThroughput);
      }
      metrics.put(lockAcquireStats.getName(), lockAcquireStats);
      metrics.put(lockReleaseStats.getName(), lockReleaseStats);
      metrics.put(lockTimeOutStats.getName(), lockTimeOutStats);

      return new SuccessResult(metrics);
    }
  }
}
