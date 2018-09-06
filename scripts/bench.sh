#!/bin/sh

bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd -P "$bin">/dev/null; pwd`
home="$(dirname "$bin")"
jars=$home"/jars/*"

echo $bin
echo $jars
echo $home

java -cp "$jars" org.apache.bench.Cmd $@

