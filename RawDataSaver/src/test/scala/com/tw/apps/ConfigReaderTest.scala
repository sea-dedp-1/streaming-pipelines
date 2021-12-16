package com.tw.apps

import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.api.{BackgroundPathable, GetDataBuilder}
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class ConfigReaderTest extends FeatureSpec with Matchers with GivenWhenThen with MockFactory {
  feature("Get spark stream configs") {
    scenario("Success") {
      Given("zkClient and zookeeperFolder")
      val zkClient = mock[CuratorFramework]
      val mockBuilder = mock[GetDataBuilder]
      val mockWatcher = mock[BackgroundPathable[Array[Byte]]]
      val zookeeperFolder: String = "someFolder"
      (zkClient.getData _) stubs() returning (mockBuilder)
      (mockBuilder.watched _) stubs() returning (mockWatcher)
      (mockBuilder.forPath _) stubs (s"$zookeeperFolder/kafkaBrokers") returning ("testBrokerPath".getBytes)
      (mockWatcher.forPath _) stubs (s"$zookeeperFolder/topic") returning ("testTopicPath".getBytes)
      (mockWatcher.forPath _) stubs (s"$zookeeperFolder/checkpointLocation") returning ("testCheckpointLocationPath".getBytes)
      (mockWatcher.forPath _) stubs (s"$zookeeperFolder/dataLocation") returning ("testDataLocationPath".getBytes)

      When("getting spark stream configs")
      val (kafkaBrokers, topic, checkpointLocation, dataLocation) = ConfigReader.getSparkStreamConfigs(zkClient, zookeeperFolder)

      Then("configs are retrieved correctly")
      kafkaBrokers should be("testBrokerPath")
      topic should be("testTopicPath")
      checkpointLocation should be("testCheckpointLocationPath")
      dataLocation should be("testDataLocationPath")
    }
  }
}
