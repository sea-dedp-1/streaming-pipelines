#!/usr/bin/env bash

set -eu

RAW_DATA_FOLDER=${1}

TODAY=$(date +"%Y-%m-%d")

docker exec -it streamingdatapipeline_hadoop_1 \
  /usr/local/hadoop/bin/hadoop fs -ls "/tw/rawData/${RAW_DATA_FOLDER}/data/date=${TODAY}" \
  > /dev/null
