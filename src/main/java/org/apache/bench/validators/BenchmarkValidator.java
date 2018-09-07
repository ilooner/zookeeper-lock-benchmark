package org.apache.bench.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import com.google.common.collect.Lists;
import org.apache.bench.BenchmarkRegistry;

import java.util.Collections;
import java.util.List;

public class BenchmarkValidator implements IParameterValidator {
  @Override
  public void validate(String name, String value) throws ParameterException {
    if (BenchmarkRegistry.REGISTRY.containsKey(value)) {
      // This is a valid benchmark
      return;
    }

    List<String> names = Lists.newArrayList(BenchmarkRegistry.REGISTRY.keySet());
    Collections.sort(names);

    final String validBenchmarks = String.join(", ", names);
    final String message = String.format("The given benchmark name (%s) is invalid. " +
      "Please specify one of the following benchmarks:\n%s", value, validBenchmarks);

    throw new ParameterException(message);
  }
}
