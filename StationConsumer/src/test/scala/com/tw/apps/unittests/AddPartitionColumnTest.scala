package com.tw.apps.unittests

import com.tw.apps.StationDataTransformationUtils.StationDataDataframe
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import org.scalatest._

import java.util.TimeZone
import scala.collection.JavaConversions._

class AddPartitionColumnTest extends FeatureSpec with Matchers with GivenWhenThen {

  feature("Apply partition transformations to data frame") {
    val initialTzId = TimeZone.getDefault.toZoneId
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    val spark = SparkSession.builder.appName("Test App").master("local").getOrCreate()

    val schema = StructType(List(
      StructField("bikes_available",IntegerType,false),
      StructField("docks_available",IntegerType,false),
      StructField("is_renting",BooleanType,false),
      StructField("is_returning",BooleanType,false),
      StructField("last_updated",LongType,false),
      StructField("station_id",StringType,false),
      StructField("name",StringType,false),
      StructField("latitude",DoubleType,false),
      StructField("longitude",DoubleType,false)
    ))

    val data = Seq(
      Row(14, 4, true, true, 1639755555L, "3210", "Pershing Field", 40.742677141, -74.051788633),
      Row(11, 8, true, true, 1639755555L, "3606", "49 Ave & 21 St", 40.74252, -73.948852),
      Row(7, 9, true, true, 1640155967L, "bc0b154a7de61364485deb1bb518f006", "24th St at Bartlett St", 37.7520708, -122.41997372800698),
      Row(5, 9, true, true, 1639755555L, "3949", "Brook Ave & E 138 St", 40.807408, -73.91924)
    )

    val initialDf = spark.createDataFrame(data, schema)

    scenario("Add Partition Columns for valid data") {

      Given("Pre-transformed data for Station Consumer")


      When("Partition Columns are Added")

      val resultDf = initialDf.addPartition()

      Then("The following columns are expected")

      val expectedSchema = schema.add(StructField("dt",StringType,true))
                                  .add(StructField("hour",StringType,true))

      val expectedData = Seq(
        Row(14, 4, true, true, 1639755555L, "3210", "Pershing Field", 40.742677141, -74.051788633, "2021-12-17", "15"),
        Row(11, 8, true, true, 1639755555L, "3606", "49 Ave & 21 St", 40.74252, -73.948852, "2021-12-17", "15"),
        Row(7, 9, true, true, 1640155967L, "bc0b154a7de61364485deb1bb518f006", "24th St at Bartlett St", 37.7520708, -122.41997372800698, "2021-12-22", "06"),
        Row(5, 9, true, true, 1639755555L, "3949", "Brook Ave & E 138 St", 40.807408, -73.91924, "2021-12-17", "15")
      )
// hive table already created create table schema partion by dt, hour
      val expectedDf = spark.createDataFrame(expectedData, expectedSchema)

      resultDf.schema shouldBe expectedDf.schema
      resultDf.collect shouldBe expectedDf.collect

      // teardown
      TimeZone.setDefault(TimeZone.getTimeZone(initialTzId))

    }

    scenario("Add Partition Columns for invalid data") {
      Given("Null timestamp on last updated column for Station Consumer")
      val data = Seq(
        Row(14, 4, true, true, null, "3210", "Pershing Field", 40.742677141, -74.051788633)
      )
      val initialDf = spark.createDataFrame(data, schema)

      When("Partition Columns are Added")

      val resultDf = initialDf.addPartition()

      Then("The dt and hour columns will be null")

      val expectedSchema = schema.add(StructField("dt",StringType,true))
        .add(StructField("hour",StringType,true))

      val expectedData = Seq(
        Row(14, 4, true, true, null, "3210", "Pershing Field", 40.742677141, -74.051788633, null, null)
      )

      val expectedDf = spark.createDataFrame(expectedData, expectedSchema)

      resultDf.collect shouldBe expectedDf.collect

      // teardown
      TimeZone.setDefault(TimeZone.getTimeZone(initialTzId))

    }
  }
}
