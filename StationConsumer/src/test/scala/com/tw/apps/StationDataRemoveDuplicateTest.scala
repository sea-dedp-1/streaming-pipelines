package com.tw.apps

import com.tw.apps.StationDataTransformationUtils.StationDataDataset
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import org.scalatest._

import scala.collection.JavaConversions._

class StationDataRemoveDuplicateTest extends FeatureSpec with Matchers with GivenWhenThen {

  feature("Apply Deduplication to Dataframe") {

    scenario("should remove duplicated data") {

      Given("Duplicated data")

      val spark = SparkSession.builder.appName("Test App").master("local").getOrCreate()
      import spark.implicits._
      val schema = StructType(List(
        StructField("bikes_available", IntegerType, false),
        StructField("docks_available", IntegerType, false),
        StructField("is_renting", BooleanType, false),
        StructField("is_returning", BooleanType, false),
        StructField("last_updated", LongType, false),
        StructField("station_id", StringType, false),
        StructField("name", StringType, false),
        StructField("latitude", DoubleType, false),
        StructField("longitude", DoubleType, false)
      ))

      val data = Seq(
        Row(14, 4, true, true, 1639755555L, "3210", "Pershing Field", 40.742677141, -74.051788633),
        Row(11, 8, true, true, 1639755555L, "3606", "49 Ave & 21 St", 40.74252, -73.948852),
        Row(7, 9, true, true, 1640155967L, "bc0b154a7de61364485deb1bb518f006", "24th St at Bartlett St", 37.7520708, -122.41997372800698),
        Row(7, 9, true, true, 1640155967L, "bc0b154a7de61364485deb1bb518f006", "24th St at Bartlett St", 37.7520708, -122.41997372800698),
        Row(5, 9, true, true, 1639755555L, "3949", "Brook Ave & E 138 St", 40.807408, -73.91924)
      )

      val initialDf = spark.createDataFrame(data, schema)
      val stationData = initialDf.as[StationData]

      When("Duplicates are removed")

      val deduplicatedData = stationData.removeDuplicates()(spark)

      Then("Expected count should be correct")

      deduplicatedData.count shouldBe 4
    }
  }

}
