package com.tw.apps

import StationDataTransformation._
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.spark.sql.SparkSession
import StationDataTransformationUtils.StationDataDataset

object StationApp {

  def main(args: Array[String]): Unit = {

    val zookeeperConnectionString = if (args.isEmpty) "zookeeper:2181" else args(0)

    val retryPolicy = new ExponentialBackoffRetry(1000, 3)

    val zkClient = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy)

    zkClient.start()

    val sfStationBrokers = new String(zkClient.getData.forPath("/tw/stationDataSF/kafkaBrokers"))
    val nycStationBrokers = new String(zkClient.getData.forPath("/tw/stationDataNYCV2/kafkaBrokers"))

    val nycStationTopic = new String(zkClient.getData.watched.forPath("/tw/stationDataNYCV2/topic"))
    val sfStationTopic = new String(zkClient.getData.watched.forPath("/tw/stationDataSF/topic"))

    val checkpointLocation = new String(
      zkClient.getData.watched.forPath("/tw/output/checkpointLocation"))

    val outputLocation = new String(
      zkClient.getData.watched.forPath("/tw/output/dataLocation"))

    val spark = SparkSession.builder
      .appName("StationConsumer")
      .getOrCreate()

    import spark.implicits._

    val nycStationDF = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", nycStationBrokers)
      .option("subscribe", nycStationTopic)
      .option("startingOffsets", "latest")
      .option("failOnDataLoss", "false")
      .load()
      .selectExpr("CAST(value AS STRING) as raw_payload")
      .transform(cityBikesStationStatusJson2DF(_, spark))

    val sfStationDF = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", sfStationBrokers)
      .option("subscribe", sfStationTopic)
      .option("startingOffsets", "latest")
      .option("failOnDataLoss", "false")
      .load()
      .selectExpr("CAST(value AS STRING) as raw_payload")
      .transform(cityBikesStationStatusJson2DF(_, spark))

    nycStationDF
      .union(sfStationDF)
      .as[StationData]
      .removeDuplicates()(spark)
      .writeStream
      .format("overwriteCSV")
      .outputMode("complete")
      .option("header", true)
      .option("truncate", false)
      .option("checkpointLocation", checkpointLocation)
      .option("path", outputLocation)
      .start()
      .awaitTermination()

  }
}