#!/bin/bash

set -e

$hadoop_path fs -fs hdfs://$hdfs_server -mkdir -p /tw/rawData/stationDataSF/checkpoints \
&& $hadoop_path fs -fs hdfs://$hdfs_server -mkdir -p /tw/rawData/stationDataSF/data \
&& $hadoop_path fs -fs hdfs://$hdfs_server -mkdir -p /tw/rawData/stationDataNYCV2/checkpoints \
&& $hadoop_path fs -fs hdfs://$hdfs_server -mkdir -p /tw/rawData/stationDataNYCV2/data \
&& $hadoop_path fs -fs hdfs://$hdfs_server -mkdir -p /tw/stationMart/checkpoints \
&& $hadoop_path fs -fs hdfs://$hdfs_server -mkdir -p /tw/stationMart/data
