// build.sbt
import sbtassembly.AssemblyPlugin.autoImport._

lazy val root = (project in file("."))
  .settings(
    name := "HelloWorldApp",
    version := "0.1",
    scalaVersion := "2.12.15", // Use your Scala version
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-sql" % "3.3.2" // Use your Spark version
    ),
    mainClass in assembly := Some("HelloWorld"),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case PathList("org", "apache", "commons", "logging", xs @ _*) => MergeStrategy.first
      case PathList("org", "apache", "spark", "unused", xs @ _*) => MergeStrategy.first
      case _ => MergeStrategy.first
    }
  )