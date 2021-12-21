#!/usr/bin/env bash

set -e

clean_up () {
  make down_e2e
}

trap clean_up EXIT

make e2e

echo "===== e2e test for e2e output csv data====="

bash ./scripts/retry.sh \
  ./scripts/e2e-verify-csv-hdfs.sh 10 60

for RAW_DATA_FOLDER in stationDataMarseille stationDataSF stationInformation stationStatus
do
  echo "===== e2e test for HDFS raw data: ${RAW_DATA_FOLDER} ====="

  bash ./scripts/retry.sh \
    "./scripts/e2e-test-raw-data-saver.sh ${RAW_DATA_FOLDER}"
done
