val dottyLatestNightly = dottyLatestNightlyBuild.get
//val dottyVersion = dottyLatestNightly 
//val dottyVersion = "0.24.0-bin-20200320-30f8c6f-NIGHTLY"
val dottyVersion = "0.23.0-RC1"
val scala2Version = "2.13.1"

inThisBuild(Seq(
  organization := "org.typelevel",
  scalaVersion := dottyVersion,
  crossScalaVersions := Seq(dottyVersion, dottyLatestNightly, scala2Version),
  updateOptions := updateOptions.value.withLatestSnapshots(false),
))

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-Xfatal-warnings",
    "-Yexplicit-nulls"
  ),

  libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
  aggregate in doc := false,
  publishArtifact in (Compile, packageDoc) := false
)

lazy val noPublishSettings =
  skip in publish := true

lazy val root = project.in(file("."))
  .aggregate(core)
  .dependsOn(core)
  .settings(commonSettings:_*)
  .settings(noPublishSettings)

lazy val core = project
  .in(file("core"))
  .settings(
    moduleName := "shapeless-core",
  )
  .settings(commonSettings: _*)

lazy val local = project
  .in(file("local"))
  .dependsOn(core)
  .settings(
    moduleName := "shapeless-local",
    scalacOptions ++= List("-Xmax-inlines", "1000"),
    //scalacOptions += "-Xprint:posttyper",
    scalacOptions in console in Compile -= "-Xprint:posttyper",
    initialCommands in console := """import shapeless._ ; import scala.deriving._"""
  )
  .settings(commonSettings: _*)
