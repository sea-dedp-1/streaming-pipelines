package com.tw.apps

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.spark.sql.functions._

object StationLocationApp {
  def main(args: Array[String]): Unit = {

    val retryPolicy = new ExponentialBackoffRetry(1000, 3)

    val (zookeeperConnectionString: String, zookeeperFolder: String) = extractArgs(args)

    val zkClient = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy)

    zkClient.start

    val (kafkaBrokers: String, topic: String, checkpointLocation: String, dataLocation: String) = getSparkStreamConfigs(zkClient, zookeeperFolder)

    val spark = SparkSession.builder
      .appName("RawDataSaver")
      .getOrCreate()

    val savedStream = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", kafkaBrokers)
      .option("subscribe", topic)
      .option("startingOffsets", "latest")
      .option("failOnDataLoss", false)
      .load()

    val transformedStream: DataFrame = addTimestampCol(savedStream)

    transformedStream.writeStream
      .partitionBy("date")
      .outputMode("append")
      .format("parquet")
      .option("checkpointLocation", checkpointLocation)
      .option("path", dataLocation)
      .start()
      .awaitTermination()
  }

  private def addTimestampCol(savedStream: DataFrame) = {
    val transformedStream = savedStream
      .selectExpr("CAST(value AS STRING) as raw_payload")
      .withColumn("date", date_format(current_date(), "yyyy-MM-dd"))
    transformedStream
  }

  def extractArgs(args: Array[String]) = {
    if (args.length != 2) {
      val message = "Two arguments are required: \"zookeeper server\" and \"application folder in zookeeper\"!"
      throw new IllegalArgumentException(message)
    }
    val zookeeperConnectionString = args(0)

    val zookeeperFolder = args(1)
    (zookeeperConnectionString, zookeeperFolder)
  }

  def getSparkStreamConfigs(zkClient: CuratorFramework, zookeeperFolder: String) = {
    val kafkaBrokers = new String(zkClient.getData.forPath(s"$zookeeperFolder/kafkaBrokers"))

    val topic = new String(zkClient.getData.watched.forPath(s"$zookeeperFolder/topic"))

    val checkpointLocation = new String(
      zkClient.getData.watched.forPath(s"$zookeeperFolder/checkpointLocation"))

    val dataLocation = new String(
      zkClient.getData.watched.forPath(s"$zookeeperFolder/dataLocation"))

    (kafkaBrokers, topic, checkpointLocation, dataLocation)
  }
}
