#!/usr/bin/env bash

set -u

RAW_DATA_FOLDER=${1}

TODAY=$(date +"%Y-%m-%d")

print_pass () {
  echo -e "\033[0;32;1mPASSED\033[0m"
}

print_fail () {
  echo -e "\033[0;31;1mFAILED\033[0m"
}

printf "Integration test for HDFS raw data: ${RAW_DATA_FOLDER}\t"

RAW_DATA_FOUND=$(
  docker exec -it streamingdatapipeline_hadoop_1 \
    /usr/local/hadoop/bin/hadoop fs -ls "/tw/rawData/${RAW_DATA_FOLDER}/data/date=${TODAY}" \
    > /dev/null;
  echo $?
)

if [ "$RAW_DATA_FOUND" = 0 ] ; then
  print_pass
else
  print_fail
fi
