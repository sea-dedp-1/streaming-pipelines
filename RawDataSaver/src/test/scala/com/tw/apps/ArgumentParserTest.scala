package com.tw.apps

import com.tw.apps.ArgumentParser.extractArgs
import org.scalatest._

class ArgumentParserTest extends FeatureSpec with Matchers with GivenWhenThen {
  feature("Extract Arguments") {
    scenario("Success") {
      Given("An array of two arguments")
      val args = Array("connection_string", "folder")

      When("An array is passed to extractArgs")
      val (zookeeperConnectionString, zookeeperFolder) = extractArgs(args)

      Then("Args are parsed correctly ")
      zookeeperConnectionString should be("connection_string")
      zookeeperFolder should be("folder")
    }

    scenario("Failure") {
      Given("An array of one argument")
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
}
