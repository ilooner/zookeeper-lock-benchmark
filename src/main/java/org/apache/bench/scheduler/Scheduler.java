package org.apache.bench.scheduler;

import org.apache.bench.queue.DistributedQueue;
import org.apache.bench.semaphore.DistributedSemaphore.DistributedLease;
import org.apache.bench.QueueBench.Query;

import java.util.concurrent.TimeUnit;

public interface Scheduler {
  DistributedLease schedule(Query qry, DistributedQueue queue, long time, TimeUnit timeUnit) throws Exception;
}
