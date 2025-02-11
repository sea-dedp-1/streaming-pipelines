#!/usr/bin/env bash
set -e

cat zookeeper.properties

echo $PATH

PATH=$PATH nohup zookeeper-server-start.sh zookeeper.properties 1> /tmp/zookeeper.log  2>/tmp/zookeeper.error.log &

nohup kafka-server-start.sh kafka.properties 1> /tmp/kafka.log  2>/tmp/kafka.error.log &

start-master.sh -p 7077

start-slave.sh 127.0.0.1:7077

mkdir -p /tmp/spark-events && start-history-server.sh

mkdir -p /tmp/tw/rawData/stationSanFrancisco/checkpoints
mkdir -p /tmp/tw/rawData/stationSanFrancisco/data
mkdir -p /tmp/tw/rawData/stationDataMarseille/checkpoints
mkdir -p /tmp/tw/rawData/stationDataMarseille/data
mkdir -p /tmp/tw/stationMart/checkpoints
mkdir -p /tmp/tw/stationMart/data



zk_command="zookeeper-shell.sh 127.0.0.1:2181"

$zk_command create /tw ''

$zk_command create /tw/stationDataSF ''
$zk_command create /tw/stationDataSF/kafkaBrokers 127.0.0.1:9092
$zk_command create /tw/stationDataSF/topic station_data_sf
$zk_command create /tw/stationDataSF/checkpointLocation /tmp/tw/rawData/stationDataSF/checkpoints
$zk_command create /tw/stationDataSF/dataLocation /tmp/tw/rawData/stationDataSF/data

$zk_command create /tw/stationDataMarseille ''
$zk_command create /tw/stationDataMarseille/kafkaBrokers 127.0.0.1:9092
$zk_command create /tw/stationDataMarseille/topic station_data_marseille
$zk_command create /tw/stationDataMarseille/checkpointLocation /tmp/tw/rawData/stationDataMarseille/checkpoints
$zk_command create /tw/stationDataMarseille/dataLocation /tmp/tw/rawData/stationDataMarseille/data

$zk_command create /tw/stationDataNYCV2 ''
$zk_command create /tw/stationDataNYCV2/kafkaBrokers 127.0.0.1:9092
$zk_command create /tw/stationDataNYCV2/topic station_data_nyc_v2
$zk_command create /tw/stationDataNYCV2/checkpointLocation /tmp/tw/rawData/stationDataNYCV2/checkpoints
$zk_command create /tw/stationDataNYCV2/dataLocation /tmp/tw/rawData/stationDataNYCV2/data

$zk_command create /tw/output ''
$zk_command create /tw/output/checkpointLocation /tmp/tw/stationMart/checkpoints
$zk_command create /tw/output/dataLocation /tmp/tw/stationMart/data


tail -f /dev/null
