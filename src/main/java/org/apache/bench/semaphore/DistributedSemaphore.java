package org.apache.bench.semaphore;

import java.util.concurrent.TimeUnit;

public interface DistributedSemaphore {
  DistributedLease acquire(long time, TimeUnit unit) throws Exception;

  interface DistributedLease extends AutoCloseable{}
}
