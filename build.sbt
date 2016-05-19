
lazy val root = (project in file(".")).settings(
    name := "SparkStreaming1",
    organization := "abhi",
    version := "1.0",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-streaming" % "1.6.0" % "provided",
      "org.apache.spark" %% "spark-core" % "1.6.0" % "provided",
      "org.apache.spark" %% "spark-streaming-twitter" % "1.6.0"
    ),
    mainClass in assembly := Some("com.abhi.TendingHashTags"),
    assemblyMergeStrategy in assembly := {
      case PathList(ps @ _*) if ps.last == "UnusedStubClass.class" => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )
