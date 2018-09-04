package org.apache.bench;

import java.util.Map;

public interface Benchmark {
  void run();

  interface Factory<T extends Benchmark> {
    T create(Map<String, String> config);
  }
}
