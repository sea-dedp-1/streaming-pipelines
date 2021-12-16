package com.tw.apps

import org.apache.spark.sql.functions.{current_date, date_format}
import org.apache.spark.sql.{DataFrame, SparkSession}

object RawDataTransformer {
  implicit class RawDataDataframe(val df: DataFrame) {
    def addTimestampColumn(): DataFrame = {
      df
        .selectExpr("CAST(value AS STRING) as raw_payload")
        .withColumn("date", date_format(current_date(), "yyyy-MM-dd"))
    }
  }
}
