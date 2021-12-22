package com.tw.apps

import org.apache.spark.sql.functions.{col, from_unixtime}
import org.apache.spark.sql.DataFrame

object StationDataPartition {
  implicit class StationDataDataframe(val df: DataFrame) {
    def addPartition(): DataFrame = {
        df
          .withColumn("dt", from_unixtime(col("last_updated"), "yyyy-MM-dd"))
          .withColumn("hour", from_unixtime(col("last_updated"), "HH"))
    }

  }
}
