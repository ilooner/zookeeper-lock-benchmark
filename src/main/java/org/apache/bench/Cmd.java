package org.apache.bench;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class Cmd {
  public static void main(String[] args) {
    final CmdArgs cmdArgs = new CmdArgs();

    JCommander instance = JCommander.newBuilder()
      .addObject(cmdArgs)
      .build();
    try {
      instance.parse(args);
    } catch (ParameterException e) {
      if (e.getMessage() != null) {
        System.err.println(e.getMessage());
      }

      System.out.println("\n\n");

      instance.usage();
      System.exit(0);
    }

    final Benchmark benchmark = cmdArgs.getBenchmarkFactory().create(cmdArgs);
    final Benchmark.Result result = benchmark.run();

    switch (result.getType()) {
      case SUCCESS:
        printSuccess((Benchmark.SuccessResult) result);
        break;
      case FAILURE:
        printFailure((Benchmark.FailureResult) result);
        break;
    }
  }

  public static void printSuccess(Benchmark.SuccessResult result) {
    List<String> metricNames = Lists.newArrayList(result.getMetrics().keySet());
    Collections.sort(metricNames);

    for (String metricName: metricNames) {
      System.out.println(metricName + ": " + result.getMetrics().get(metricName));
    }
  }

  public static void printFailure(Benchmark.FailureResult result) {
    if (result.getMessage() != null) {
      System.err.println(result.getMessage());
    }

    for (Exception exception: result.getExceptions()) {
      exception.printStackTrace();
    }
  }
}
