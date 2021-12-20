#!/usr/bin/env bash

while getopts "t:" flag
do
    case "${flag}" in
        t) TOPIC=${OPTARG};;
    esac
done

echo "===== Integration Test for Kafka Topic: $TOPIC ====="

echo "Starting docker exec command..."
KAFKA_COMMAND="kafka-console-consumer.sh --zookeeper zookeeper:2181 --topic $TOPIC --max-messages 1 --timeout-ms 60000"
KAFKA_MESSAGE=$(docker exec -t streamingdatapipeline_kafka_1 /bin/bash -c "$KAFKA_COMMAND")

echo "Checking for metadata in kafka message..."
IS_TOPIC_FOUND=$(echo "$KAFKA_MESSAGE" | grep -q "metadata"; echo $?)

if [ "$IS_TOPIC_FOUND" = 0 ]
  then
    echo "PASSED integration test for topic: $TOPIC"
  else
    echo "FAILED integration test for topic: $TOPIC"
fi

exit "$IS_TOPIC_FOUND"