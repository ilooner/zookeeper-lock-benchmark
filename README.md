## Tool Usage

To see the usage run

```
./bench.sh
```

 - **Duration:** The duration is specified using the [java.time.Duration](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-) syntax. 

## Testing Locally

First build the tool

```
mvn clean install
```

Start zookeeper in docker

```
./scripts/start.sh
```

Run the tool

```
./target/zookeeper-bench-1.0-SNAPSHOT/zookeeper-bench-1.0-SNAPSHOT/bin/bench.sh -b lockMutate --connection '127.0.0.1:2181' -d PT60S -n 1 -t 0
```

## Distribution

First build

```
mvn clean install
```

A tarball containing all the scripts and jars will be generated in `target/zookeeper-bench-XXX.tar.gz`.
