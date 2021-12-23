package com.tw.apps

import org.apache.spark.sql.{Dataset}

object StationDataValidation {
  def validateLatLong(df: Dataset[StationData], errorDF: Dataset[StationData]): (Dataset[StationData], Dataset[StationData]) = {
    val validatedDF = df.filter(x => x.latitude != null && x.longitude != null)
    val newErrorDF = df.filter(x => x.latitude == null || x.longitude == null)

    (validatedDF, errorDF.union(newErrorDF))
  }

  def validateBikesDocksAvailable(df: Dataset[StationData], errorDF: Dataset[StationData]): (Dataset[StationData], Dataset[StationData]) = {
    val validatedDF = df.filter(x => x.bikes_available >= 0 && x.docks_available >= 0)
    val newErrorDF = df.filter(x => x.bikes_available < 0 || x.docks_available < 0)

    (validatedDF, errorDF.union(newErrorDF))
  }

  def validate(df: Dataset[StationData], errorDF: Dataset[StationData]): (Dataset[StationData], Dataset[StationData]) = {
    val (validatedLatLongDF, errorLatLogDF) = validateLatLong(df, errorDF)

    validateBikesDocksAvailable(validatedLatLongDF, errorLatLogDF)
  }
}

