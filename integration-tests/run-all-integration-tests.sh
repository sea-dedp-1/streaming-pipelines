#!/usr/bin/env bash

set -eux

./integration-tests/test-kafka-producer.sh -t station_data_marseille
./integration-tests/test-kafka-producer.sh -t station_data_sf
./integration-tests/test-kafka-producer.sh -t station_information
./integration-tests/test-kafka-producer.sh -t station_status

./integration-tests/test-raw-data-saver.sh stationDataMarseille
./integration-tests/test-raw-data-saver.sh stationDataSF
./integration-tests/test-raw-data-saver.sh stationInformation
./integration-tests/test-raw-data-saver.sh stationStatus
