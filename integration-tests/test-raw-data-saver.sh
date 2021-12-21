#!/usr/bin/env bash

set -u

RAW_DATA_FOLDER=${1}

TODAY=$(date +"%Y-%m-%d")

TEST_NAME="Integration test for HDFS raw data: ${RAW_DATA_FOLDER}"
echo "===== ${TEST_NAME} ====="

WAIT_INTERVAL_IN_SECONDS=10
TRIES=0
MAX_TRIES=18

# Try for up to 3 minutes
until [[ "${TRIES}" -ge "${MAX_TRIES}" ]]
do
  HADOOP_LS_COMMAND="/usr/local/hadoop/bin/hadoop fs -ls /tw/rawData/${RAW_DATA_FOLDER}/data/date=${TODAY}"
  docker exec -it streamingdatapipeline_hadoop_1 ${HADOOP_LS_COMMAND} > /dev/null
  RAW_DATA_FOUND=$?

  if [ "$RAW_DATA_FOUND" = 0 ] ; then
    echo -e "\033[0;32;1mPASSED\033[0m ${TEST_NAME}"
    exit 0
  fi

  TRIES=$((TRIES+1))
  echo "[Try ${TRIES} / ${MAX_TRIES}] No data found in ${RAW_DATA_FOLDER}. Retrying..."
  sleep ${WAIT_INTERVAL_IN_SECONDS}
done

echo -e "\033[0;31;1mFAILED\033[0m ${TEST_NAME}"
exit 1
