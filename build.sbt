name := "spark-getting-started"
organization := "ai.ayyildiz"
version := "0.7.0-pre"

scalaVersion := "2.12.17"

fork / run := true
run / javaOptions ++= Seq(
  "-Dlog4j.debug=true",
  "-Dlog4j.configuration=log4j.properties"
)

val sparkVersion        = "3.3.1"
val standfordNlpVersion = "4.5.1"

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql"               % sparkVersion,
  "org.apache.spark" %% "spark-mllib"             % sparkVersion,
  "org.apache.spark" %% "spark-streaming"         % sparkVersion,
  "org.apache.bahir" %% "spark-streaming-twitter" % "2.4.0" excludeAll ExclusionRule(organization = "org.apache.spark"),
  "edu.stanford.nlp"  % "stanford-corenlp"        % standfordNlpVersion,
  "edu.stanford.nlp"  % "stanford-corenlp"        % standfordNlpVersion classifier "models",
  "org.scalatest"    %% "scalatest"               % "3.2.14" % "test"
)
