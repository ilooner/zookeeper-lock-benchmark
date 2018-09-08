package org.apache.bench.semaphore;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;

import java.util.concurrent.TimeUnit;

public class ZkDistributedSemaphore implements DistributedSemaphore{
  static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ZkDistributedSemaphore.class);

  final InterProcessSemaphoreV2 semaphore;

  public ZkDistributedSemaphore(CuratorFramework client, String path, int numberOfLeases) {
    this.semaphore = new InterProcessSemaphoreV2(client, path, numberOfLeases);
  }

  @Override
  public DistributedLease acquire(long time, TimeUnit unit) throws Exception {
    Lease lease = semaphore.acquire(time, unit);
    if(lease != null){
      return new LeaseHolder(lease);
    }else{
      return null;
    }
  }

  private class LeaseHolder implements DistributedLease {
    Lease lease;

    public LeaseHolder(Lease lease) {
      super();
      this.lease = lease;
    }

    @Override
    public void close() throws Exception {
      lease.close();
    }
  }
}
