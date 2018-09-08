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

  public DistributedQueue(String name, long cpu, long memory,
                          String QUEUE_PATH, CuratorFramework client) {
    this.name = name;
    this.cpu = cpu;
    this.memory = memory;
    this.semaphore = new ZkDistributedSemaphore(client, QUEUE_PATH + "/" + name + "/semaphore",8 );
    this.lock = new InterProcessMutex(client, QUEUE_PATH + "/" + name + "/lock");
  }


  public DistributedLease enqueue() throws Exception {
    return this.semaphore.acquire(5, TimeUnit.MINUTES);
  }

  public void release(DistributedLease lease) throws Exception {
    lease.close();
  }
}
