package com.tw.apps

import org.apache.spark.sql.SparkSession
import org.scalatest._

class StationDataValidationTest extends FeatureSpec with Matchers with GivenWhenThen {

  feature("Validate transformed raw data ") {
    val spark = SparkSession.builder.appName("Test App").master("local").getOrCreate()
    import spark.implicits._

    val validData = Seq(
      StationData(14, 4, is_renting = true, is_returning = true, 1639755555L, "3210", "Pershing Field", 40.807408, -122.41997372800698),
      StationData(11, 8, is_renting = true, is_returning = true, 1639755555L, "3606", "49 Ave & 21 St", 40.807408, -122.41997372800698)
    )

    scenario("Validate that lat and long fields in data is not null") {

      Given("Transformed data for Station Consumer contains invalid lat/long values")

      val invalidData = Seq(
        StationData(7, 9, is_renting = true, is_returning = true, 1640155967L, "bc0b154a7de61364485deb1bb518f006", "24th St at Bartlett St", null, -122.41997372800698),
        StationData(5, 9, is_renting = true, is_returning = true, 1639755555L, "3949", "Brook Ave & E 138 St", 40.807408, null),
        StationData(5, 9, is_renting = true, is_returning = true, 1639755555L, "3949", "Brook Ave & E 138 St", null, null)
      )

      val data = validData ++ invalidData
      val rawDF = data.toDS()

      When("Validate Lat Long fields not null")
      val (validatedDF, errorDF) = StationDataValidation.validateLatLong(rawDF, spark.emptyDataset)

      Then("Validated DF contains valid rows")
      validatedDF.collect should be(validData.toArray)

      Then("Error DF will contain rows with null lat")
      errorDF.collect should be(invalidData)
    }
  }
}
