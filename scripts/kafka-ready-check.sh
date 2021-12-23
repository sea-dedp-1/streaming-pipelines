#!/usr/bin/env bash

set -e

docker exec -it streamingdatapipeline_kafka_1 kafka-topics.sh --list --zookeeper zookeeper:2181
