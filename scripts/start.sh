#!/bin/sh

docker run --rm -p 2181:2181 --name zookeeper-test zookeeper:3.4.12
