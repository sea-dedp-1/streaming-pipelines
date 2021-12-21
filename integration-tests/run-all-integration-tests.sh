#!/usr/bin/env bash

set -e

clean_up () {
  make down_integration_test
}

trap clean_up EXIT

make integration_test

for TOPIC in station_data_marseille station_data_sf station_information station_status
do
  echo "===== Integration test for Kafka Topic: ${TOPIC} ====="

  bash ./scripts/retry.sh \
    "./scripts/integration-test-kafka-producer.sh -t ${TOPIC}"
done

for RAW_DATA_FOLDER in stationDataMarseille stationDataSF stationInformation stationStatus
do
  echo "===== Integration test for HDFS raw data: ${RAW_DATA_FOLDER} ====="

  bash ./scripts/retry.sh \
    "./scripts/e2e-test-raw-data-saver.sh ${RAW_DATA_FOLDER}"
done
