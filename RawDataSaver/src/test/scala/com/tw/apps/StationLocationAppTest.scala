package com.tw.apps

import com.tw.apps.StationLocationApp.{extractArgs, getSparkStreamConfigs}
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.api.{BackgroundPathable, GetDataBuilder}
import org.scalamock.scalatest.MockFactory
import org.scalatest._


class StationLocationAppTest extends FeatureSpec with Matchers with GivenWhenThen with MockFactory {

  feature("Extract Arguements") {
    scenario("Success") {
      Given("An array of two arguements")
      val args = Array("connection_string", "folder")

      When("An array is passed to extractArgs")
      val (zookeeperConnectionString, zookeeperFolder) = extractArgs(args)

      Then("Args are parsed correctly ")
      zookeeperConnectionString should be("connection_string")
      zookeeperFolder should be("folder")
    }

    scenario("Failure") {
      Given("An array of one argurment")
      val args = Array("connection_string")

      When("An array is passed to extractArgs")


      Then("Args are parsed correctly ")
      val caught = intercept[IllegalArgumentException] {
        extractArgs(args)
      }
      val message = "Two arguments are required: \"zookeeper server\" and \"application folder in zookeeper\"!"
      caught.getMessage should be(message)
    }
  }

  feature("Get spark stream configs") {
    scenario("Success") {
      Given("zkClient and zookeeperFolder")
      val zkClient = mock[CuratorFramework]
      val mockBuilder = mock[GetDataBuilder]
      val mockWatcher = mock[BackgroundPathable[Array[Byte]]]
      val zookeeperFolder: String = "someFolder"
      (zkClient.getData _) stubs () returning (mockBuilder)
      (mockBuilder.watched _) stubs () returning (mockWatcher)
      (mockBuilder.forPath _) stubs (s"$zookeeperFolder/kafkaBrokers") returning ("testBrokerPath".getBytes)
      (mockWatcher.forPath _) stubs (s"$zookeeperFolder/topic") returning ("testTopicPath".getBytes)
      (mockWatcher.forPath _) stubs (s"$zookeeperFolder/checkpointLocation") returning ("testCheckpointLocationPath".getBytes)
      (mockWatcher.forPath _) stubs (s"$zookeeperFolder/dataLocation") returning ("testDataLocationPath".getBytes)

      When("getting spark stream configs")
      val (kafkaBrokers, topic, checkpointLocation, dataLocation) = getSparkStreamConfigs(zkClient, zookeeperFolder)

      Then("configs are retrieved correctly")
      kafkaBrokers should be("testBrokerPath")
      topic should be("testTopicPath")
      checkpointLocation should be("testCheckpointLocationPath")
      dataLocation should be("testDataLocationPath")
    }
  }

}