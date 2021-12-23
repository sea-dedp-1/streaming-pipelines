#!/usr/bin/env bash

set -e

REPO_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "Building JARs..."
bash ${REPO_DIR}/sbin/buildAll.sh

echo "Bringing up environment..."

${REPO_DIR}/docker/docker-compose-integration-test.sh \
  up -d \
  kafka \
  zookeeper \
  zookeeper-seed \
  hadoop \
  hadoop-seed

bash ${REPO_DIR}/scripts/retry.sh ${REPO_DIR}/scripts/kafka-ready-check.sh

echo "write to hdfs"

${REPO_DIR}/docker/docker-compose-integration-test.sh \
  up -d --no-deps \
  station-consumer

CONSUMER_READY_CHECK_WAIT_INTERVAL=30
CONSUMER_READY_CHECK_MAX_RETRIES=14
bash ${REPO_DIR}/scripts/retry.sh \
  ${REPO_DIR}/scripts/consumer-ready-check.sh \
  ${CONSUMER_READY_CHECK_WAIT_INTERVAL} \
  ${CONSUMER_READY_CHECK_MAX_RETRIES}

echo "Setting up Kafka topics..."

docker exec streamingdatapipeline_kafka_1 bash -c \
  "kafka-console-producer.sh --broker-list localhost:9092 --topic station_data_sf < /tmp/mock_station_data_sf_kafka_message.json"
docker exec streamingdatapipeline_kafka_1 bash -c \
  "kafka-console-producer.sh --broker-list localhost:9092 --topic station_data_nyc_v2 < /tmp/mock_station_data_nyc_kafka_message.json"
