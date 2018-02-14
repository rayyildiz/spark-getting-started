name := "getting-started"
organization := "com.rayyildiz"
version := "0.3.0"

scalaVersion := "2.11.12"

fork in run := true
javaOptions in run ++= Seq(
  "-Dlog4j.debug=true",
  "-Dlog4j.configuration=log4j.properties"
)

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"

val sparkVersion = "2.3.0"
val standfordNlpVersion = "3.8.0"


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "databricks" % "spark-corenlp" % "0.2.0-s_2.11",
  "org.apache.bahir" %% "spark-streaming-twitter" % "2.2.0",

  "edu.stanford.nlp" % "stanford-corenlp" % standfordNlpVersion,
  "edu.stanford.nlp" % "stanford-corenlp" % standfordNlpVersion classifier "models"
)