#!/bin/bash

# Verify the raw data server not empty
RESULT=`docker exec -it streamingdatapipeline_hadoop_1 /usr/local/hadoop/bin/hadoop fs -cat /tw/stationMart/data/part-*.csv`
if [ "$RESULT" != "" ]; then
      echo PASSED
   else
      echo FAILED
      exit 1
fi
