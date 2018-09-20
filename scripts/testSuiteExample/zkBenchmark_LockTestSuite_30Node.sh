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
echo "Running test: lockMutate_Node30Thread1"
/root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --build --srcPath /root/zookeeper-benchmark/zookeeper-lock-benchmark --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName lockMutate_Node30Thread1 --logDir /tmp/zkBench/testResults --benchArgs "-b lockMutate --connection '10.10.120.101:5181' -d PT120S -n 1 -x"
sleep 2
echo "Running test: lockMutate_Node30Thread4"
root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName lockMutate_Node30Thread4 --logDir /tmp/zkBench/testResults --benchArgs "-b lockMutate --connection '10.10.120.101:5181' -d PT120S -n 4 -x"
sleep 2
echo "Running test: lockMutate_Node30Thread8"
root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters  --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName lockMutate_Node30Thread8 --logDir /tmp/zkBench/testResults --benchArgs "-b lockMutate --connection '10.10.120.101:5181' -d PT120S -n 8 -x"
sleep 2
echo "Running test: lockMutate_Node30Thread16"
root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName lockMutate_Node30Thread16 --logDir /tmp/zkBench/testResults --benchArgs "-b lockMutate --connection '10.10.120.101:5181' -d PT120S -n 16 -x"
sleep 2
echo "Running test: lockMutate_Node30Thread32"
root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/zkBenchmarkExecute.sh --tarPath /root/zookeeper-benchmark/zookeeper-lock-benchmark/target/zookeeper-bench-1.0-SNAPSHOT.tar.gz --clushGroup bench --destPath /root/zkBenchmark --numIter $numIters --sTime 1 --nodePrefix "10.10.120" --nodeStart 102 --nodeEnd 131 --testName lockMutate_Node30Thread32 --logDir /tmp/zkBench/testResults --benchArgs "-b lockMutate --connection '10.10.120.101:5181' -d PT120S -n 32 -x"
sleep 2

iterToPick="$(($numIters - 1))"
echo "Test Suite Completed."
echo "Copying iteration $iterToPick results to aggregated directory $aggDir"
cp -R /tmp/zkBench/testResults/lockMutate_*/lockMutate_*"-${iterToPick}" $aggDir
echo "Copied all the results to aggregated directory"
echo "Aggregating all the results and dumping the final files"
python /root/zookeeper-benchmark/zookeeper-lock-benchmark/scripts/parse-log.py $aggDir
