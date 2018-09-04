package org.apache.bench;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

public class BenchmarkRegistry {
  public static final Map<String, Benchmark.Factory> REGISTRY = ((Supplier<Map<String, Benchmark.Factory>>) () -> {
    Map<String, Benchmark.Factory> registry = Maps.newHashMap();

    registry.put(LockAndMutateBench.NAME, new LockAndMutateBench.Factory());
    registry.put(QueueBench.NAME, new QueueBench.Factory());
    registry.put(QueueLockAndMutateBench.NAME, new QueueLockAndMutateBench.Factory());

    return Collections.unmodifiableMap(registry);
  }).get();
}
