package com.tw.apps

import org.apache.spark.sql.functions.{col, from_unixtime}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object StationDataTransformationUtils {
  implicit class StationDataDataframe(val df: DataFrame) {

    def addPartition(): DataFrame =
        df.withColumn("dt", from_unixtime(col("last_updated"), "yyyy-MM-dd"))
          .withColumn("hour", from_unixtime(col("last_updated"), "HH"))

  }

  implicit class StationDataDataset(val df: Dataset[StationData]) {

    def removeDuplicates()(implicit spark: SparkSession): Dataset[StationData] = {

      import spark.implicits._

      df.groupByKey(r => r.station_id)
        .reduceGroups((r1, r2) => if (r1.last_updated > r2.last_updated) r1 else r2)
        .map(_._2)
    }
  }
}
