name := "spark-getting-started"
organization := "dev.rayyildiz"
version := "0.5.0"

scalaVersion := "2.12.8"

fork in run := true
javaOptions in run ++= Seq(
  "-Dlog4j.debug=true",
  "-Dlog4j.configuration=log4j.properties"
)

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven/"

val sparkVersion = "2.4.3"
val standfordNlpVersion = "3.9.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "databricks" % "spark-corenlp" % "0.4.0-spark2.4-scala2.11" excludeAll ExclusionRule(organization = "org.apache.spark"),
  "org.apache.bahir" % "spark-streaming-twitter_2.11" % "2.3.3" excludeAll ExclusionRule(organization = "org.apache.spark"),
  "edu.stanford.nlp" % "stanford-corenlp" % standfordNlpVersion,
  "edu.stanford.nlp" % "stanford-corenlp" % standfordNlpVersion classifier "models",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)
