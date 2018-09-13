package org.apache.bench.queue;

import org.apache.bench.semaphore.DistributedSemaphore;
import org.apache.bench.semaphore.DistributedSemaphore.DistributedLease;
import org.apache.bench.semaphore.ZkDistributedSemaphore;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

public class DistributedQueue {
  private final String name;
  private final long cpu;
  private final long memory;
  private final DistributedSemaphore semaphore;
  private final InterProcessMutex lock;
  private final String queue_path;
  private final String queue_lock_path;

  public static class QueueLease {
    private final String queuename;
    private final DistributedLease lease;

    public QueueLease(String queuename, DistributedLease lease) {
      this.queuename = queuename;
      this.lease = lease;
    }

    public String queueName() {
      return this.queuename;
    }

    public DistributedLease getDistributedLease() {
      return this.lease;
    }
  }

  public DistributedQueue(String name, long cpu, long memory, int numOfLeases,
                          String QUEUE_PATH, CuratorFramework client) {
    this.name = name;
    this.cpu = cpu;
    this.memory = memory;
    this.queue_path = QUEUE_PATH + "/" + name + "/semaphore";
    this.queue_lock_path = QUEUE_PATH + "/" + name + "/lock";
    this.semaphore = new ZkDistributedSemaphore(client, queue_path,numOfLeases );
    this.lock = new InterProcessMutex(client, queue_lock_path);
  }

  public DistributedLease enqueue(long time, TimeUnit unit) throws Exception {
    return this.semaphore.acquire(time, unit);
  }

  public void release(DistributedLease lease) throws Exception {
    lease.close();
  }

  public void close(CuratorFramework client) throws Exception {
    if (client.checkExists().forPath(queue_path) != null) {
      client.delete().deletingChildrenIfNeeded().forPath(queue_path);
    }

    if (client.checkExists().forPath(queue_lock_path) != null) {
      client.delete().deletingChildrenIfNeeded().forPath(queue_lock_path);
    }
  }
}
