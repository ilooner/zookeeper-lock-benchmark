package org.apache.bench;

import com.beust.jcommander.Parameter;
import org.apache.bench.ZKBlobDataGen.DataDefConstants;
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

  @Parameter(names = { "--throughputPerMs", "-tps" }, validateWith = NonNegativeLongValidator.class,
    description = "Required throughput per ms")
  public long requiredThroughput;

  @Parameter(names = { "--noTransaction", "-x" }, description = "Run acquire/release test with no transaction request")
  public boolean noTransaction;

  @Parameter(names = { "--nodeCount", "-nc" }, validateWith = PositiveIntegerValidator.class, description = "Number " +
    "of nodes to generate data for.")
  public int nodeCount = DataDefConstants.DEFAULT_NODE_COUNT;

  @Parameter(names = { "--verbose", "-v" }, description = "Print verbose result data for each client results along " +
    "with aggregate")
  public boolean verbose;

  @Parameter(names = { "--minSleepTimeMs", "-mt" }, description = "Minimum sleep time to mimic computation during " +
    "transaction lock. A time between minSleepTimeMs and maxSleepTimeMs is chosen.",
    validateWith = PositiveIntegerValidator.class)
  public int minSleepTimeInMs = 10;

  @Parameter(names = { "--maxSleepTimeMs", "-Mt" }, description = "Minimum sleep time to mimic computation during " +
    "transaction lock. A time between minSleepTimeMs and maxSleepTimeMs is chosen.",
    validateWith = PositiveIntegerValidator.class)
  public int maxSleepTimeInMs = 500;

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

  public boolean isTransactionsDisabled() {
    return noTransaction;
  }

  public boolean printVerbose() {
    return verbose;
  }

  public int getNodeCount() {
    return nodeCount;
  }

  public int getMinSleepTimeInMs() {
    return minSleepTimeInMs;
  }

  public int getMaxSleepTimeInMs() {
    return maxSleepTimeInMs;
  }
}
