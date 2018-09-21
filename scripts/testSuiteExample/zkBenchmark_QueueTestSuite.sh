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
echo "Running test: queue_Node1Thread1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread1_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread1_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread1_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread1_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 10"

echo "Running test: queue_Node1Thread4"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread4_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread4_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread4_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread4_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1 -nl 10"

cho "Running test: queue_Node1Thread8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread8_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread8_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread8_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread8_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 10"

echo "Running test: queue_Node1Thread16"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread16_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread16_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread16_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench1 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67" --testName queue_Node1Thread16_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 10"

# 2 Node Tests
echo "Running test: queue_Node2Thread1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread1_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread1_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread1_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread1_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 10"


echo "Running test: queue_Node2Thread4"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread4_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread4_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread4_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread4_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1 -nl 10"

echo "Running test: queue_Node2Thread8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread8_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread8_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread8_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread8_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 10"

echo "Running test: queue_Node2Thread16"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread16_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread16_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread16_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench2 --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68" --testName queue_Node2Thread16_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 10"

# 4 Node Tests
echo "Running test: queue_Node4Thread1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread1_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread1_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread1_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread1_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 1 -mqet 0 -Mqet 0 -qc 1 -nl 10"

echo "Running test: queue_Node4Thread4"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread4_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1  -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread4_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1  -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread4_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1  -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread4_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 4 -mqet 0 -Mqet 0 -qc 1  -nl 10"

echo "Running test: queue_Node4Thread8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread8_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread8_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread8_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread8_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 8 -mqet 0 -Mqet 0 -qc 1 -nl 10"

echo "Running test: queue_Node4Thread16"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread16_lease1 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 1"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread16_lease6 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 6"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread16_lease8 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 8"
./zkBenchmarkExecute.sh --build --srcPath /root/zkBenchmarkRepo/zookeeper-lock-benchmark/ --clushGroup bench --destPath /root/zkBenchmark/ --numIter 2 --sTime 1 --nodes "10.10.103.67, 10.10.103.68, 10.10.103.69, 10.10.103.70" --testName queue_Node4Thread16_lease10 --logDir /tmp/queueBench --benchArgs "-b queue --connection '10.10.103.67:2181' -d PT120S -n 16 -mqet 0 -Mqet 0 -qc 1 -nl 10"

iterToPick="$(($numIters - 1))"
echo "Test Suite Completed"
echo "Copying iteration $iterToPick results to aggregated directory $aggDir"
cp -R /tmp/queueBench/queue_Node*/queue_Node*"-${iterToPick}" $aggDir
echo "Copied all the results to aggregated directory"
python /root/zkBenchmarkRepo/zookeeper-lock-benchmark/scripts/parse-log.py $aggDir
