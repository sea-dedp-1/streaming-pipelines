#!/bin/sh
echo $zk_command
$zk_command rmr /tw
$zk_command create /tw ''

$zk_command create /tw/stationDataNYC ''
$zk_command create /tw/stationDataNYC/topic e2e_station_data_nyc
$zk_command create /tw/stationDataNYC/checkpointLocation hdfs://$hdfs_server/tw/rawData/stationDataNYC/checkpoints

$zk_command create /tw/stationInformation ''
$zk_command create /tw/stationInformation/kafkaBrokers $kafka_server
$zk_command create /tw/stationInformation/topic e2e_station_information
$zk_command create /tw/stationInformation/checkpointLocation hdfs://$hdfs_server/tw/rawData/stationInformation/checkpoints
$zk_command create /tw/stationInformation/dataLocation hdfs://$hdfs_server/tw/rawData/stationInformation/data

$zk_command create /tw/stationStatus ''
$zk_command create /tw/stationStatus/kafkaBrokers $kafka_server
$zk_command create /tw/stationStatus/topic e2e_station_status
$zk_command create /tw/stationStatus/checkpointLocation hdfs://$hdfs_server/tw/rawData/stationStatus/checkpoints
$zk_command create /tw/stationStatus/dataLocation hdfs://$hdfs_server/tw/rawData/stationStatus/data

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

$zk_command create /tw/output ''
$zk_command create /tw/output/checkpointLocation hdfs://$hdfs_server/tw/stationMart/checkpoints
$zk_command create /tw/output/dataLocation hdfs://$hdfs_server/tw/stationMart/data
