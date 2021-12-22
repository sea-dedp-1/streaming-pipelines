#!/usr/bin/env bash

set -e

WAIT_INTERVAL=10
MAX_RETRIES=18
FOLDERS_TO_VALIDATE=(stationDataMarseille stationDataSF stationInformation stationStatus stationDataNYCV2)

echo "===== Waiting for Hadoop to be ready ====="
bash ./scripts/retry.sh \
  "./scripts/hadoop-healthcheck.sh" ${WAIT_INTERVAL} ${MAX_RETRIES}

for RAW_DATA_FOLDER in ${FOLDERS_TO_VALIDATE[@]}
do
  echo "===== e2e test for HDFS raw data: ${RAW_DATA_FOLDER} ====="

  bash ./scripts/retry.sh \
    "./scripts/e2e-test-raw-data-saver.sh ${RAW_DATA_FOLDER}" ${WAIT_INTERVAL} ${MAX_RETRIES}
done

echo "===== e2e test for e2e output csv data====="

bash ./scripts/retry.sh \
  "./scripts/e2e-verify-csv-hdfs.sh" ${WAIT_INTERVAL} ${MAX_RETRIES}
