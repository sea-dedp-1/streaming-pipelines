#!/bin/sh
echo $zk_command
$zk_command rmr /tw
$zk_command create /tw ''

$zk_command create /tw/stationDataSF ''
$zk_command create /tw/stationDataSF/kafkaBrokers $kafka_server
$zk_command create /tw/stationDataSF/topic e2e_station_data_sf
$zk_command create /tw/stationDataSF/checkpointLocation hdfs://$hdfs_server/tw/rawData/stationDataSF/checkpoints
$zk_command create /tw/stationDataSF/dataLocation hdfs://$hdfs_server/tw/rawData/stationDataSF/data

$zk_command create /tw/stationDataMarseille ''
$zk_command create /tw/stationDataMarseille/kafkaBrokers $kafka_server
$zk_command create /tw/stationDataMarseille/topic e2e_station_data_marseille
$zk_command create /tw/stationDataMarseille/checkpointLocation hdfs://$hdfs_server/tw/rawData/stationDataMarseille/checkpoints
$zk_command create /tw/stationDataMarseille/dataLocation hdfs://$hdfs_server/tw/rawData/stationDataMarseille/data

$zk_command create /tw/stationDataNYCV2 ''
$zk_command create /tw/stationDataNYCV2/kafkaBrokers $kafka_server
$zk_command create /tw/stationDataNYCV2/topic e2e_station_data_nyc_v2
$zk_command create /tw/stationDataNYCV2/checkpointLocation hdfs://$hdfs_server/tw/rawData/stationDataNYCV2/checkpoints
$zk_command create /tw/stationDataNYCV2/dataLocation hdfs://$hdfs_server/tw/rawData/stationDataNYCV2/data

$zk_command create /tw/output ''
$zk_command create /tw/output/checkpointLocation hdfs://$hdfs_server/tw/stationMart/checkpoints
$zk_command create /tw/output/dataLocation hdfs://$hdfs_server/tw/stationMart/data
