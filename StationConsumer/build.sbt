val sparkVersion = "2.3.0"
val testcontainersScalaVersion = "0.39.11"

val kafkaVersion = "0.10.0.1"
lazy val excludeJpountz = ExclusionRule(organization = "net.jpountz.lz4", name = "lz4")

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.tw",
      scalaVersion := "2.11.8",
      version := "0.0.1"
    )),

    name := "tw-station-consumer",

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.5" % "test",
      "org.apache.kafka" %% "kafka" % kafkaVersion % "test" excludeAll (excludeJpountz),
      "org.apache.curator" % "curator-test" % "2.10.0" % "test",
      "org.apache.spark" %% "spark-core" % sparkVersion,
      "org.apache.spark" %% "spark-sql" % sparkVersion,
      "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion excludeAll (excludeJpountz),
      "org.apache.spark" %% "spark-streaming" % sparkVersion,
      "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion excludeAll (excludeJpountz),
      "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersScalaVersion % "test"
    ),

    Test / fork := true

)
