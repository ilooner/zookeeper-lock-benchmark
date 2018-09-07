package org.apache.bench;

import com.beust.jcommander.Parameter;
import org.apache.bench.validators.BenchmarkValidator;
import org.apache.bench.validators.DurationValidator;
import org.apache.bench.validators.NonNegativeLongValidator;
import org.apache.bench.validators.PositiveIntegerValidator;

import java.time.Duration;

public class CmdArgs {
  @Parameter(names = { "--connection", "-c" }, required = true, description = "Zookeeper servers to connect to.")
  public String connectionString;

  @Parameter(names = { "--numClients", "-n" }, required = true, validateWith = PositiveIntegerValidator.class,
    description = "Number of zookeeper clients to use.")
  public int numClients;

  @Parameter(names = { "--benchmark", "-b" }, required = true, validateWith = BenchmarkValidator.class,
    description = "Name of the benchmark to use.")
  public String benchmark;

  @Parameter(names = { "--duration", "-d" }, required = true, validateWith = DurationValidator.class,
    description = "Duration of the benchmark in java.time.Duration format.")
  public String duration;

  @Parameter(names = { "--throughputPerMs", "-t" }, required = true, validateWith = NonNegativeLongValidator.class,
    description = "Required throughput per ms")
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
