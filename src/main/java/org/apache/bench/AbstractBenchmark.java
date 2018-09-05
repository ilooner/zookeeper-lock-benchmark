package org.apache.bench;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractBenchmark implements Benchmark {
  private final CmdArgs config;

  public AbstractBenchmark(final CmdArgs config) {
    this.config = Preconditions.checkNotNull(config);
  }

  @Override
  public Result run() {
    final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(
      Executors.newFixedThreadPool(config.getNumClients()));

    List<Task> tasks = Lists.newArrayList();
    List<ListenableFuture<Result>> futures = Lists.newArrayList();

    for (int taskCount = 0; taskCount < config.getNumClients(); taskCount++) {
      final Task task = createTask(config);
      final ListenableFuture<Result> future = executorService.submit(task);

      tasks.add(task);
      futures.add(future);
    }

    long endTime = System.currentTimeMillis() + config.getDurationInMillis();
    long currentTime;

    List<FailureResult> failures = Lists.newArrayList();
    List<SuccessResult> successes = Lists.newArrayList();

    while ((currentTime = System.currentTimeMillis()) < endTime) {
      long sleepTime = endTime - currentTime;
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        failures.add(new FailureResult("Terminating the benchmark early."));
        break;
      }
    }

    if (failures.isEmpty()) {
      ListenableFuture<List<Result>> uberFuture = Futures.allAsList(futures);
      List<Result> results = null;

      try {
        results = uberFuture.get(1, TimeUnit.MINUTES);
      } catch (InterruptedException | ExecutionException | TimeoutException e) {
        failures.add(new FailureResult(Lists.newArrayList(e)));
      }

      if (failures.isEmpty()) {
        results.forEach(result -> {
          switch (result.getType()) {
            case SUCCESS:
              successes.add((SuccessResult) result);
            case FAILURE:
              failures.add((FailureResult) result);
          }
        });
      }
    }

    if (!failures.isEmpty()) {
      tasks.forEach(task -> task.terminate());
    }

    executorService.shutdown();

    try {
      executorService.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      FailureResult failureResult = new FailureResult(
        "Timed out while shutting down executor service",
        Lists.newArrayList(e));

      failures.add(failureResult);
    }

    if (!failures.isEmpty()) {
      return FailureResult.aggregate(failures);
    }

    // Compute aggregate metrics
    return null;
  }

  public abstract Task createTask(final CmdArgs cmdArgs);

  public static CuratorFramework createClient(final CmdArgs cmdArgs) {
    final RetryPolicy policy = new RetryNTimes(3, 10000);
    final CuratorFramework client = CuratorFrameworkFactory.newClient(cmdArgs.getConnectionString(), policy);
    return client;
  }

  public static abstract class AbstractTask implements Task {
    private final CmdArgs cmdArgs;
    private volatile boolean terminated = false;

    public AbstractTask(final CmdArgs cmdArgs) {
      this.cmdArgs = Preconditions.checkNotNull(cmdArgs);
    }

    @Override
    public Result call() throws Exception {
      CuratorFramework client = null;
      List<Exception> exceptionList = new ArrayList<>();
      Map<String, Double> metrics = null;

      try {
        client = createClient(cmdArgs);
        client.start();
        metrics = runTask(client);
      } catch (Exception ex) {
        exceptionList.add(ex);
      } finally {
        if (client != null) {
          try {
            client.close();
          } catch (Exception exClose) {
            exceptionList.add(exClose);
          }
        }
      }

      if (!exceptionList.isEmpty() || metrics == null) {
        return new FailureResult(exceptionList);
      } else {
        return new SuccessResult(metrics);
      }
    }

    public abstract Map<String, Double> runTask(CuratorFramework client);

    protected boolean isTerminated() {
      return terminated;
    }

    @Override
    public void terminate() {
      this.terminated = true;
    }
  }
}
