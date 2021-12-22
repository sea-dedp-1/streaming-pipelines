package com.tw.apps

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.types.StructType
import org.scalatest._
import scala.collection.JavaConversions._

class AddPartitionColumnTest extends FeatureSpec with Matchers with GivenWhenThen {


  feature("Apply station status transformations to data frame") {


    val spark = SparkSession.builder.appName("Test App").master("local").getOrCreate()

    val schema = ScalaReflection.schemaFor[StationData].dataType.asInstanceOf[StructType]

    val data = Seq(
      Row(14, 4, true, true, 1639755555L, "3210", "Pershing Field", 40.742677141, -74.051788633),
      Row(11, 8, true, true, 1639755555L, "3606", "49 Ave & 21 St", 40.74252, -73.948852),
      Row(7, 9, true, true, 1640155967L, "bc0b154a7de61364485deb1bb518f006", "24th St at Bartlett St", 37.7520708, -122.41997372800698),
      Row(5, 9, true, true, 1639755555L, "3949", "Brook Ave & E 138 St", 40.807408, -73.91924)
    )

    val initialDf = spark.createDataFrame(data, schema)


    scenario("Add Partition Columns") {

      Given("Pre-transformed data for Station Consumer")


      When("Partition Columns are Added")

      val resultDf = ???

      Then("The following columns are expected")

      val expectedSchema = ScalaReflection.schemaFor[PartitionedStationData].dataType.asInstanceOf[StructType]

      val expectedData = Seq(
        Row(14, 4, true, true, 1639755555L, "3210", "Pershing Field", 40.742677141, -74.051788633, "2021-12-17", "15"),
        Row(11, 8, true, true, 1639755555L, "3606", "49 Ave & 21 St", 40.74252, -73.948852, "2021-12-17", "15"),
        Row(7, 9, true, true, 1640155967L, "bc0b154a7de61364485deb1bb518f006", "24th St at Bartlett St", 37.7520708, -122.41997372800698, "2021-12-22", "06"),
        Row(5, 9, true, true, 1639755555L, "3949", "Brook Ave & E 138 St", 40.807408, -73.91924, "2021-12-17", "15")
      )

      val expectedDf = spark.createDataFrame(expectedData, expectedSchema)

      resultDf.schema shouldBe expectedDf.schema
      resultDf.collect shouldBe expectedDf.collect
    }
  }

}
