package com.tw.apps

import com.tw.apps.RawDataTransformer.RawDataDataframe
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.spark.sql.SparkSession

object StationLocationApp {
  def main(args: Array[String]): Unit = {

    val retryPolicy = new ExponentialBackoffRetry(1000, 3)

    val (zookeeperConnectionString: String, zookeeperFolder: String) = ArgumentParser.extractArgs(args)

    val zkClient = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy)

    zkClient.start

    val (kafkaBrokers: String, topic: String, checkpointLocation: String, dataLocation: String) = ConfigReader.getSparkStreamConfigs(zkClient, zookeeperFolder)

    val spark = SparkSession.builder
      .appName("RawDataSaver")
      .getOrCreate()

    spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", kafkaBrokers)
      .option("subscribe", topic)
      .option("startingOffsets", "latest")
      .option("failOnDataLoss", false)
      .load()
      .addTimestampColumn
      .writeStream
      .partitionBy("date")
      .outputMode("append")
      .format("parquet")
      .option("checkpointLocation", checkpointLocation)
      .option("path", dataLocation)
      .start()
      .awaitTermination()
  }
}
