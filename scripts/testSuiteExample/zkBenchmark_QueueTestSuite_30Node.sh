#!/bin/sh

aggDir=$1
numIters=$2
if [ -z "$aggDir" ]; then
   echo "Directory to store aggregated changes is not specified"
   echo "Usage:./zkBenchmark_LockTestSuite.sh <aggregated result directory> <number of iterations per test>"
   exit 1
fi

if [ -z "$numIters" ]; then
   echo "Number of iterations per test is not specified"
   echo "Usage:./zkBenchmark_LockTestSuite.sh <aggregated result directory> <number of iterations per test>"
   exit 1
fi

# 30 Node Tests
echo "Running test: queue_Node30Thread1"
/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --build --srcPath /root/zookeeper-benchmark/zookeeper-lock-benchmark --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread1_qc2_lease10 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 2 -nl 10"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread1_qc2_lease15 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 2 -nl 15"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread1_qc1_lease10 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 10"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread1_qc1_lease15 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 15"

sleep 2

echo "Running test: queue_Node30Thread4"
/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread4_qc2_lease10 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 2 -nl 10"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread4_qc2_lease15 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 2 -nl 15"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread4_qc1_lease10 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1 -nl 10"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread4_qc1_lease15  --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1 -nl 15"

sleep 2

echo "Running test: queue_Node30Thread8"
/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters  --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread8_qc2_lease10 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 2 -nl 10"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters  --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread8_qc2_lease15 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 2 -nl 15"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters  --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread8_qc1_lease10 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 10"


sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters  --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread8_qc1_lease15 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 15"

sleep 2

echo "Running test: queue_Node30Thread16"
/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread16_qc2_lease10 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 2 -nl 10"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread16_qc2_lease15 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 2 -nl 15"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread16_qc1_lease10 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 10"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread16_qc1_lease15 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 15"

sleep 2

echo "Running test: queue_Node30Thread32"
/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath  /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread32_qc2_lease10 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 32 -mqet 0 -Mqet 0 -qc 2 -nl 10"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread32_qc2_lease15 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 32 -mqet 0 -Mqet 0 -qc 2 -nl 15"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread32_qc1_lease10 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 32 -mqet 0 -Mqet 0 -qc 1 -nl 10"

sleep 2

/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName queue_Node30Thread32_qc1_lease15 --logDir /tmp/zkBench/queue/testResults --benchArgs "-b queue --connection '10.10.120.101:5181' -d PT120S -n 32 -mqet 0 -Mqet 0 -qc 1 -nl 15"

sleep 2

iterToPick="$(($numIters - 1))"
echo "Test Suite Completed."
echo "Copying iteration $iterToPick results to aggregated directory $aggDir"
cp -R /tmp/zkBench/queue/testResults/queue_Node*/queue_Node*"-${iterToPick}" $aggDir
echo "Copied all the results to aggregated directory"
echo "Aggregating all the results and dumping the final files"
python /root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/parse-log.py $aggDir
