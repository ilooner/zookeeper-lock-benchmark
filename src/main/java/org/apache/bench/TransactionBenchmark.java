/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.bench;

import com.google.common.base.Stopwatch;
import org.apache.bench.ZKBlobDataGen.BlobData;
import org.apache.bench.ZKBlobDataGen.DataDefConstants;
import org.apache.bench.ZKBlobDataGen.NodeFreeUsedResourceData;
import org.apache.bench.ZKBlobDataGen.NodeQueryClusterUtilization;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TransactionBenchmark extends AbstractBenchmark {
  //private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TransactionBenchmark.class);
  public static final String NAME = "transaction";
  public static final String TRANSACTION_TASK_NAME = "TransactionTasks";

  public static final String BASE_PATH = "/org/apache/zookeeperbench/" + TransactionBenchmark.NAME;
  public static final String BLOB_PATH_1 = BASE_PATH + "/blob1";
  public static final String BLOB_PATH_2 = BASE_PATH + "/blob2";

  public static byte[] blob1Data;
  public static byte[] blob2Data;

  private TransactionBenchmark(final CmdArgs config) {
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
  }

  @Override
  protected void printBenchState() {
    System.out.println("Size of blob1 data: " + (blob1Data.length / 1024.0) + " KB");
    System.out.println("Size of blob2 data: " + (blob2Data.length / 1024.0) + " KB");
  }

  @Override
  public TransactionBenchmark.Task createTask(CmdArgs cmdArgs, int taskId, CuratorFramework client) {
    return new TransactionBenchmark.Task(cmdArgs, taskId, client);
  }

  public static class Factory implements Benchmark.Factory<TransactionBenchmark> {
    @Override
    public TransactionBenchmark create(final CmdArgs config) {
      return new TransactionBenchmark(config);
    }
  }

  public static class Task extends AbstractBenchmark.AbstractTask {

    private final Random r = new Random();

    public Task(CmdArgs cmdArgs, int taskId, CuratorFramework client) {
      super(cmdArgs, taskId, client);
    }

    @Override
    public Benchmark.Result runTask(CuratorFramework client) {
      Stopwatch timeToTransact = new Stopwatch();
      final Benchmark.TaskStatistics transactionStats = new Benchmark.TaskStatistics(TRANSACTION_TASK_NAME, taskId);

      final Map<String, Benchmark.TaskStatistics> metrics = new HashMap<>();

      while (!isTerminated()) {
        timeToTransact.start();
        try {
          // Currently just a stub
          client.getData().forPath(BLOB_PATH_1);
          client.getData().forPath(BLOB_PATH_2);

          // Choose a random time between min and max sleep time to mimic the time needed for transaction.
          // If both min and max sleep time is same then sleep for only that time
          int randomSleepTime = r.nextInt((cmdArgs.getMaxSleepTimeInMs() - cmdArgs.getMinSleepTimeInMs()) + 1) +
            cmdArgs.getMinSleepTimeInMs();
          Thread.sleep(randomSleepTime);

          Collection<CuratorTransactionResult> results = client.inTransaction()
            .setData().forPath(BLOB_PATH_1, blob1Data)
            .and()
            .setData().forPath(BLOB_PATH_2, blob2Data)
            .and()
            .commit();

          timeToTransact.stop();
          transactionStats.addSuccess(timeToTransact.elapsed(TimeUnit.NANOSECONDS));
        } catch (Exception e) {
          timeToTransact.stop();
          transactionStats.addFailure(timeToTransact.elapsed(TimeUnit.NANOSECONDS));
        } finally {
          timeToTransact.reset();
        }

        long totalThroughput = (long)transactionStats.getCurrentThroughput();
        throttleIfRequired(totalThroughput);
      }

      metrics.put(transactionStats.getName(), transactionStats);

      return new Benchmark.SuccessResult(metrics);
    }
  }
}
