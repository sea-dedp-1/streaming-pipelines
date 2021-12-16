package com.tw.apps

import com.tw.apps.RawDataTransformer.RawDataDataframe
import org.apache.spark.sql.SparkSession
import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class RawDataTransformerTest extends FeatureSpec with Matchers with GivenWhenThen {
  feature("Add timestamp column to data") {
    val spark: SparkSession = SparkSession.builder
      .appName("Spark Test App")
      .config("spark.driver.host", "127.0.0.1")
      .master("local")
      .getOrCreate()

    import spark.implicits._

    val rawData = Seq("""{"key": "value"}""")
    val df = rawData.toDF()

    val timestampedDF = df.addTimestampColumn()

    timestampedDF.columns should contain theSameElementsAs Seq("raw_payload", "date")
    timestampedDF.count() should be(1)
  }
}
