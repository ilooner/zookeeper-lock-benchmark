package org.apache.bench;

import com.beust.jcommander.Parameter;

import java.time.Duration;

public class CmdArgs {
  @Parameter(names = { "--connection", "-c" }, description = "Zookeeper servers to connect to.")
  public String connectionString;

  @Parameter(names = { "--numClients", "-n" }, description = "Number of zookeeper clients to use.")
  public int numClients;

  @Parameter(names = { "--benchmark", "-b" }, description = "Name of the benchmark to use.")
  public String benchmark;

  @Parameter(names = { "--duration", "-d" }, description = "Duration of the benchmark in java.time.Duration format.")
  public String duration;

  @Parameter(names = { "--throughputPerMs", "-t" }, description = "Required throughput per ms")
  public long requiredThroughput;


  public String getConnectionString() {
    return connectionString;
  }

  public int getNumClients() {
    return numClients;
  }

  public Benchmark.Factory getBenchmarkFactory() {
    return BenchmarkRegistry.REGISTRY.get(benchmark);
  }

  public long getDurationInMillis() {
    return Duration.parse(duration).getSeconds() * 1000L;
  }

  public long getRequiredThrougput() {
    return (requiredThroughput == 0) ? Long.MAX_VALUE : requiredThroughput;
  }
}
