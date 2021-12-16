package com.tw.apps

object ArgumentParser {
  def extractArgs(args: Array[String]) = {
    if (args.length != 2) {
      val message = "Two arguments are required: \"zookeeper server\" and \"application folder in zookeeper\"!"
      throw new IllegalArgumentException(message)
    }
    val zookeeperConnectionString = args(0)

    val zookeeperFolder = args(1)
    (zookeeperConnectionString, zookeeperFolder)
  }
}
