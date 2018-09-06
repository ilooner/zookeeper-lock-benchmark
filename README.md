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
