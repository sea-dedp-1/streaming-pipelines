#!/usr/bin/env bash

while getopts "t:" flag
do
    case "${flag}" in
        t) TOPIC=${OPTARG};;
    esac
done

TEST_NAME="Integration Test for Kafka Topic: ${TOPIC}"
echo "===== ${TEST_NAME} ====="

WAIT_INTERVAL_IN_SECONDS=10
TRIES=0
MAX_TRIES=18

# Try for up to 3 minutes
until [[ "${TRIES}" -ge "${MAX_TRIES}" ]]
do
  echo "Starting docker exec command..."
  KAFKA_COMMAND="kafka-console-consumer.sh --zookeeper zookeeper:2181 --topic $TOPIC --max-messages 1 --timeout-ms 60000"
  KAFKA_MESSAGE=$(docker exec -t streamingdatapipeline_kafka_1 /bin/bash -c "$KAFKA_COMMAND")

  echo "Checking for metadata in kafka message..."
  IS_TOPIC_FOUND=$(echo "$KAFKA_MESSAGE" | grep -q "metadata"; echo $?)

  if [ "$IS_TOPIC_FOUND" = 0 ]; then
    echo -e "\033[0;32;1mPASSED \033[0m ${TEST_NAME}"
    exit 0
  fi

  TRIES=$((TRIES+1))
  echo "[Try ${TRIES} / ${MAX_TRIES}] Topic ${TOPIC} not found. Retrying..."
  sleep ${WAIT_INTERVAL_IN_SECONDS}
done

echo -e "\033[0;31;1mFAILED \033[0m ${TEST_NAME}"
exit 1
