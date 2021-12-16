#!/bin/bash

# Verify the data of stationStatus in raw data server exisited
DATE=$(date +"%Y-%m-%d")
docker exec -it streamingdatapipeline_hadoop_1 /usr/local/hadoop/bin/hadoop fs -ls /tw/rawData/stationStatus/data/date=$DATE > /dev/null
if [ $? -eq 0 ]; then
      echo PASSED
   else
      echo FAILED
      exit 1
fi
