package org.apache.bench;

import com.google.common.base.Stopwatch;
import org.apache.bench.queue.DistributedQueue;
import org.apache.bench.scheduler.Scheduler;
import org.apache.bench.queue.DistributedQueue.QueueLease;
import org.apache.bench.semaphore.DistributedSemaphore.DistributedLease;
import org.apache.curator.framework.CuratorFramework;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QueueBench extends AbstractBenchmark {
  public static final String NAME = "queue";
  public static final String SMALL_QUEUE = "small";
  public static final String LARGE_QUEUE = "large";
  public static final long CLUSTER_CPU = 10;
  public static final long CLUSTER_MEMORY = 100;
  public static final String ACQUIRE_TASK_NAME = "LockAcquireTasks";
  public static final String RELEASE_TASK_NAME = "LockReleaseTasks";
  public static final String BASE_PATH = "/org/apache/zookeeperbench/";
  public static final TaskStatistics lockAcquireStats = new TaskStatistics(ACQUIRE_TASK_NAME);
  public static final TaskStatistics lockReleaseStats = new TaskStatistics(RELEASE_TASK_NAME);
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

    public QueueLease allocate(Query qry) throws Exception {
      String queuename = this.costToQueueMap.get(qry.getQueryCost());
      return new QueueLease(queuename, scheduler.schedule(qry, this.queues.get(queuename)));
    }

    public void release(QueueLease lease) throws Exception {
      this.queues.get(lease.queueName()).release(lease.getDistributedLease());
    }
  }

  public static class QueryService {
    public final int taskID;
    private int qNo;

    public QueryService(int tid) {
      this.taskID = tid;
      this.qNo = 0;
    }

    public Query buildQuery() {
      return new Query(Integer.toString(taskID) + Integer.toString(qNo++));
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

    public Query(String qID) {
      this.queryID = qID;
      this.queryTime = (long) (Math.random() * 10);
      this.queryCost = (int) (Math.random() * 2);
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
  protected void setup(CuratorFramework client) throws Exception {
    Map<String, DistributedQueue> queues = new HashMap<>();
    queues.put(SMALL_QUEUE,
            new DistributedQueue(SMALL_QUEUE, (long)(CLUSTER_CPU * 0.2),
                    (long)(CLUSTER_MEMORY * 0.2),
                    BASE_PATH, client));
    queues.put(LARGE_QUEUE,
            new DistributedQueue(LARGE_QUEUE, (long)(CLUSTER_CPU * 0.8),
                    (long)(CLUSTER_MEMORY * 0.8),
                    BASE_PATH, client));

    Map<Integer, String> costToQueueMap = new HashMap<>();
    costToQueueMap.put(0, SMALL_QUEUE);
    costToQueueMap.put(1, LARGE_QUEUE);
    this.rm = new ResourceManager(queues, new Scheduler() {
      @Override
      public DistributedLease schedule(Query qry, DistributedQueue queue) throws Exception {
        DistributedLease lease = queue.enqueue();
        //The logic of allocating the queue resources for this query goes here.
        return lease;
      }
    }, costToQueueMap);
  }

  @Override
  protected void teardown(CuratorFramework client) throws Exception {

  }

  @Override
  public SuccessResult aggregateMetrics(List<SuccessResult> metrics) {
    return null;
  }

  @Override
  public Task createTask(CmdArgs cmdArgs, int taskId) {
    return new Task(cmdArgs, this.rm, taskId);
  }

  public static class Factory implements Benchmark.Factory<QueueBench> {

    @Override
    public QueueBench create(CmdArgs config) {
      return new QueueBench(config);
    }
  }

  public static class Task extends AbstractTask {

    private final ResourceManager rm;
    private final QueryService queryService;

    public Task(CmdArgs cmdArgs, ResourceManager rm, int taskId) {
      super(cmdArgs, taskId);
      this.queryService = new QueryService(this.hashCode());
      this.rm = rm;
    }

    @Override
    public Result runTask(CuratorFramework client) {
      Stopwatch timer = new Stopwatch();

      final Map<String, TaskStatistics> metrics = new HashMap<>();

      while (!isTerminated()) {
        Query currentQuery = queryService.buildQuery();
        QueueLease lease;
        try {
          timer.start();
          lease = rm.allocate(currentQuery);
          timer.stop();
          lockAcquireStats.addSuccess(timer.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception ex) {
          timer.stop();
          lockAcquireStats.addFailure();
          continue;
        }
        queryService.execute(currentQuery);

        try {
          timer.start();
          rm.release(lease);
          timer.stop();
          lockReleaseStats.addSuccess(timer.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception ex) {
          timer.stop();
          lockReleaseStats.addFailure();
        }

        final long totalThroughput = (long) (lockAcquireStats.getCurrentThroughput() +
                lockReleaseStats.getCurrentThroughput());
        throttleIfRequired(totalThroughput);
      }
      metrics.put(lockAcquireStats.getName(), lockAcquireStats);
      metrics.put(lockReleaseStats.getName(), lockReleaseStats);

      return new SuccessResult(metrics);
    }
  }
}
