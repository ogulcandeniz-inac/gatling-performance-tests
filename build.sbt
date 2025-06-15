name := "gatling-performance-tests"

version := "0.1"

scalaVersion := "2.13.10"

enablePlugins(io.gatling.sbt.GatlingPlugin)

libraryDependencies ++= Seq(
  "io.gatling" % "gatling-core" % "3.9.4",
  "io.gatling" % "gatling-http" % "3.9.4"
)
