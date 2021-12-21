#!/usr/bin/env bash

set -eu

RAW_DATA_FOLDER=${1}

TODAY=$(date +"%Y-%m-%d")

HADOOP_LS_COMMAND="/usr/local/hadoop/bin/hadoop fs -ls /tw/rawData/${RAW_DATA_FOLDER}/data/date=${TODAY}"
docker exec -it streamingdatapipeline_hadoop_1 ${HADOOP_LS_COMMAND} > /dev/null
