package com.tw.apps

import StationDataTransformation.{nycStationStatusJson2DF, cityBikesStationStatusJson2DF}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.ScalaReflection
import org.scalatest._

class StationDataTransformationTest extends FeatureSpec with Matchers with GivenWhenThen {

  feature("Apply station status transformations to data frame") {
    val spark = SparkSession.builder.appName("Test App").master("local").getOrCreate()
    import spark.implicits._

    scenario("Transform nyc station data frame") {

      val testStationData =
        """{
          "station_id":"83",
          "bikes_available":19,
          "docks_available":41,
          "is_renting":true,
          "is_returning":true,
          "last_updated":1536242527,
          "name":"Atlantic Ave & Fort Greene Pl",
          "latitude":40.68382604,
          "longitude":-73.97632328
          }"""

      val schema = ScalaReflection.schemaFor[StationData].dataType //.asInstanceOf[StructType]

      Given("Sample data for station_status")
      val testDF1 = Seq(testStationData).toDF("raw_payload")


      When("Transformations are applied")
      val resultDF = testDF1.transform(nycStationStatusJson2DF(_, spark))

      Then("Useful columns are extracted")
      resultDF.schema.fields(0).name should be("bikes_available")
      resultDF.schema.fields(0).dataType.typeName should be("integer")
      resultDF.schema.fields(1).name should be("docks_available")
      resultDF.schema.fields(1).dataType.typeName should be("integer")
      resultDF.schema.fields(2).name should be("is_renting")
      resultDF.schema.fields(2).dataType.typeName should be("boolean")
      resultDF.schema.fields(3).name should be("is_returning")
      resultDF.schema.fields(3).dataType.typeName should be("boolean")
      resultDF.schema.fields(4).name should be("last_updated")
      resultDF.schema.fields(4).dataType.typeName should be("long")
      resultDF.schema.fields(5).name should be("station_id")
      resultDF.schema.fields(5).dataType.typeName should be("string")
      resultDF.schema.fields(6).name should be("name")
      resultDF.schema.fields(6).dataType.typeName should be("string")
      resultDF.schema.fields(7).name should be("latitude")
      resultDF.schema.fields(7).dataType.typeName should be("double")
      resultDF.schema.fields(8).name should be("longitude")
      resultDF.schema.fields(8).dataType.typeName should be("double")

      val row1 = resultDF.head()
      row1.get(0) should be(19)
      row1.get(1) should be(41)
      row1.get(2) shouldBe true
      row1.get(3) shouldBe true
      row1.get(4) should be(1536242527)
      row1.get(5) should be("83")
      row1.get(6) should be("Atlantic Ave & Fort Greene Pl")
      row1.get(7) should be(40.68382604)
      row1.get(8) should be(-73.97632328)
    }

    scenario("Transfrom citibikes data to dataframe") {
      Given("Sample data")
      val testStationData =
        """
          {
            "metadata": {
              "producer_id": "producer_station-san_francisco",
              "size": 135509,
              "message_id": "abd2461e-00b2-4a37-8f76-37298fffc454",
              "ingestion_time": 1639976291966
            },
            "payload": {
              "network": {
                "company": [
                  "Motivate LLC"
                ],
                "gbfs_href": "https://gbfs.baywheels.com/gbfs/gbfs.json",
                "href": "/v2/networks/bay-wheels",
                "id": "bay-wheels",
                "location": {
                  "city": "San Francisco Bay Area, CA",
                  "country": "US",
                  "latitude": 37.7141454,
                  "longitude": -122.25
                },
                "name": "Bay Wheels",
                "stations": [
                  {
                    "empty_slots": 11,
                    "extra": {
                      "address": null,
                      "last_updated": 1639916549,
                      "renting": 1,
                      "returning": 1,
                      "uid": "340"
                    },
                    "free_bikes": 4,
                    "id": "d0e8f4f1834b7b33a3faf8882f567ab8",
                    "latitude": 37.849735,
                    "longitude": -122.270582,
                    "name": "Harmon St at Adeline St",
                    "timestamp": "2021-12-20T04:57:19.444000Z"
                  }
                ]
              }
            }
          }
          """

      val testDF = Seq(testStationData).toDF("raw_payload")

      When("Transform with cityBikesStationStatusJson2DF")
      val resultDF = testDF.transform(cityBikesStationStatusJson2DF(_, spark))

      Then("Useful columns are extracted")
      resultDF.schema.fields(0).name should be("bikes_available")
      resultDF.schema.fields(0).dataType.typeName should be("integer")
      resultDF.schema.fields(1).name should be("docks_available")
      resultDF.schema.fields(1).dataType.typeName should be("integer")
      resultDF.schema.fields(2).name should be("is_renting")
      resultDF.schema.fields(2).dataType.typeName should be("boolean")
      resultDF.schema.fields(3).name should be("is_returning")
      resultDF.schema.fields(3).dataType.typeName should be("boolean")
      resultDF.schema.fields(4).name should be("last_updated")
      resultDF.schema.fields(4).dataType.typeName should be("long")
      resultDF.schema.fields(5).name should be("station_id")
      resultDF.schema.fields(5).dataType.typeName should be("string")
      resultDF.schema.fields(6).name should be("name")
      resultDF.schema.fields(6).dataType.typeName should be("string")
      resultDF.schema.fields(7).name should be("latitude")
      resultDF.schema.fields(7).dataType.typeName should be("double")
      resultDF.schema.fields(8).name should be("longitude")
      resultDF.schema.fields(8).dataType.typeName should be("double")

      And("Data is correct")
      val rows = resultDF.collect()
      rows should have length 1

      val row1 = rows(0)
      row1.get(0) should be(4)
      row1.get(1) should be(11)
      row1.get(2) shouldBe true
      row1.get(3) shouldBe true
      row1.get(4) should be(1639976239)
      row1.get(5) should be("d0e8f4f1834b7b33a3faf8882f567ab8")
      row1.get(6) should be("Harmon St at Adeline St")
      row1.get(7) should be(37.849735)
      row1.get(8) should be(-122.270582)
    }

    scenario("Transfrom empty citibikes data to dataframe") {
      Given("Sample data without stations")
      val testStationData =
        """
          {
            "metadata": {
              "producer_id": "producer_station-san_francisco",
              "size": 135509,
              "message_id": "abd2461e-00b2-4a37-8f76-37298fffc454",
              "ingestion_time": 1639976291966
            },
            "payload": {
              "network": {
                "company": [
                  "Motivate LLC"
                ],
                "gbfs_href": "https://gbfs.baywheels.com/gbfs/gbfs.json",
                "href": "/v2/networks/bay-wheels",
                "id": "bay-wheels",
                "location": {
                  "city": "San Francisco Bay Area, CA",
                  "country": "US",
                  "latitude": 37.7141454,
                  "longitude": -122.25
                },
                "name": "Bay Wheels"
              }
            }
          }
          """

      val testDF = Seq(testStationData).toDF("raw_payload")

      When("Transform with cityBikesStationStatusJson2DF")
      val resultDF = testDF.transform(cityBikesStationStatusJson2DF(_, spark))

      Then("Useful columns are extracted")
      resultDF.schema.fields(0).name should be("bikes_available")
      resultDF.schema.fields(0).dataType.typeName should be("integer")
      resultDF.schema.fields(1).name should be("docks_available")
      resultDF.schema.fields(1).dataType.typeName should be("integer")
      resultDF.schema.fields(2).name should be("is_renting")
      resultDF.schema.fields(2).dataType.typeName should be("boolean")
      resultDF.schema.fields(3).name should be("is_returning")
      resultDF.schema.fields(3).dataType.typeName should be("boolean")
      resultDF.schema.fields(4).name should be("last_updated")
      resultDF.schema.fields(4).dataType.typeName should be("long")
      resultDF.schema.fields(5).name should be("station_id")
      resultDF.schema.fields(5).dataType.typeName should be("string")
      resultDF.schema.fields(6).name should be("name")
      resultDF.schema.fields(6).dataType.typeName should be("string")
      resultDF.schema.fields(7).name should be("latitude")
      resultDF.schema.fields(7).dataType.typeName should be("double")
      resultDF.schema.fields(8).name should be("longitude")
      resultDF.schema.fields(8).dataType.typeName should be("double")

      val rows = resultDF.collect()
      rows should have length 0
    }
  }
}
