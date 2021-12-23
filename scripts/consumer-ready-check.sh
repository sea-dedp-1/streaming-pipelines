#!/usr/bin/env bash

set -e

[ "$(
  docker exec -it \
    streamingdatapipeline_hadoop_1 \
    /usr/local/hadoop/bin/hadoop fs -stat "/tw/stationMart/checkpoints/offsets/*" | wc -l | xargs \
)" = 2 ]
