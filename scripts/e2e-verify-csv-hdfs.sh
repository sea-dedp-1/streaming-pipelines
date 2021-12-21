#!/bin/bash

# Verify the raw data server not empty
RESULT=$(docker exec streamingdatapipeline_hadoop_1 /usr/local/hadoop/bin/hadoop fs -cat "/tw/stationMart/data/part-*.csv")

SF_ROW_COUNT=2
NYC_ROW_COUNT=1
HEADER_ROW_COUNT=1
EXPECTED_ROW_COUNT=$(( SF_ROW_COUNT+NYC_ROW_COUNT+HEADER_ROW_COUNT ))

[ "$(echo "$RESULT" | wc -l | xargs)" = $EXPECTED_ROW_COUNT ] && true
