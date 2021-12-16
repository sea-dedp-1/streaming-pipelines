package com.tw.apps

import org.apache.curator.framework.CuratorFramework


object ConfigReader {
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
