#!/usr/bin/env bash

set -e

while getopts "t:" flag
do
    case "${flag}" in
        t) TOPIC=${OPTARG};;
    esac
done

echo "Starting docker exec command..."
KAFKA_COMMAND="kafka-console-consumer.sh --zookeeper zookeeper:2181 --topic $TOPIC --max-messages 1 --timeout-ms 60000"
KAFKA_MESSAGE=$(docker exec -t streamingdatapipeline_kafka_1 /bin/bash -c "$KAFKA_COMMAND")

echo "Checking for metadata in kafka message..."
IS_TOPIC_FOUND=$(echo "$KAFKA_MESSAGE" | grep -q "metadata"; echo $?)
