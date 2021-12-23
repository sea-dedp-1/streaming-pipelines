#!/usr/bin/env bash

set -e

echo setup

./docker/docker-compose.sh \
  -f ./docker/docker-compose.yml \
  -f ./docker/docker-compose.integration-test.yml \
  up -d \
  kafka \
  zookeeper \
  zookeeper-seed \
  hadoop \
  hadoop-seed

echo "setup data"

docker exec streamingdatapipeline_kafka_1 bash -c "kafka-console-producer.sh --broker-list localhost:9092 --topic station_data_sf < /tmp/mock_station_data_sf_kafka_message.json"
docker exec streamingdatapipeline_kafka_1 bash -c "kafka-console-producer.sh --broker-list localhost:9092 --topic station_data_nyc_v2 < /tmp/mock_station_data_nyc_kafka_message.json"

echo "write to hdfs"

./docker/docker-compose.sh \
  -f ./docker/docker-compose.yml \
  -f ./docker/docker-compose.integration-test.yml \
  up -d --no-deps \
  station-consumer



# kafka-console-producer.sh --broker-list localhost:9092 --topic test_topic
# kafka-console-consumer.sh --bootstrap-server localhost:9092 --zookeeper zookeeper:2181 --topic test_topic
# kafka-console-consumer.sh --bootstrap-server localhost:9092 --zookeeper zookeeper:2181 --topic my_topic --from-beginning
# kafka-console-producer.sh --broker-list localhost:9092 --topic my_topic < test.txt
