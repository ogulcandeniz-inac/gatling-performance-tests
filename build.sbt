name := "gatling-performance-tests"

version := "0.1.0"

scalaVersion := "2.13.12"

enablePlugins(GatlingPlugin)

libraryDependencies ++= Seq(
  "io.gatling" %% "gatling-test-framework" % "3.9.5",
  "io.gatling" %% "gatling-core"           % "3.9.5"
)

// Update source directory configuration
sourceDirectory in Test := baseDirectory.value / "src" / "test"
