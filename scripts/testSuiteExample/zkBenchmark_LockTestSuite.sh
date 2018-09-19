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
 
# 1 Node tests
echo "Running test: lockMutate_Node1Thread1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67" --testName lockMutate_Node1Thread1 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 1 -x"

echo "Running test: lockMutate_Node1Thread4"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67" --testName lockMutate_Node1Thread4 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 4 -x"

echo "Running test: lockMutate_Node1Thread8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67" --testName lockMutate_Node1Thread8 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 8 -x"

echo "Running test: lockMutate_Node1Thread16"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67" --testName lockMutate_Node1Thread16 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 16 -x"

# 2 Node Tests
echo "Running test: lockMutate_Node2Thread1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName lockMutate_Node2Thread1 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 1 -x"

echo "Running test: lockMutate_Node2Thread4"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName lockMutate_Node2Thread4 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 4 -x"

echo "Running test: lockMutate_Node2Thread8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName lockMutate_Node2Thread8 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 8 -x"

echo "Running test: lockMutate_Node2Thread16"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName lockMutate_Node2Thread16 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 16 -x"

# 4 Node Tests
echo "Running test: lockMutate_Node4Thread1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName lockMutate_Node4Thread1 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 1 -x"

echo "Running test: lockMutate_Node4Thread4"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName lockMutate_Node4Thread4 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 4 -x"

echo "Running test: lockMutate_Node4Thread8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter $numIters  --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName lockMutate_Node4Thread8 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 8 -x"

echo "Running test: lockMutate_Node4Thread16"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter $numIters --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName lockMutate_Node4Thread16 --logDir /tmp/zkBench --benchArgs "-b lockMutate --connection '10.10.103.67:2181' -d PT120S -n 16 -x"

iterToPick="$(($numIters - 1))"
echo "Test Suite Completed"
echo "Copying iteration $iterToPick results to aggregated directory $aggDir"
cp -R /tmp/zkBench/lockMutate_*/lockMutate_*"-${iterToPick}" $aggDir
echo "Copied all the results to aggregated directory"
