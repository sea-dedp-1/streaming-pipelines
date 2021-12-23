package com.tw.apps

import org.apache.spark.sql.{DataFrame, Dataset, Row}

object StationDataValidation {
  def validateLatLong(df: Dataset[StationData], errorDF: Dataset[StationData]): (Dataset[StationData], Dataset[StationData]) = {
    val validatedDF = df.filter(x => x.latitude != null && x.longitude != null)
    val newErrorDF = df.filter(x => x.latitude == null || x.longitude == null)

    (validatedDF, errorDF.union(newErrorDF))
  }
}

