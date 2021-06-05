name := "spark-getting-started"
organization := "dev.rayyildiz"
version := "0.6.0-pre"

scalaVersion := "2.12.13"

fork / run := true
run / javaOptions ++= Seq(
  "-Dlog4j.debug=true",
  "-Dlog4j.configuration=log4j.properties"
)

//resolvers += "Spark Packages Repo" at "https://dl.bintray.com/spark-packages/maven/"

val sparkVersion        = "3.1.1"
val standfordNlpVersion = "4.0.0"

bintrayRepository := "pkg"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql"               % sparkVersion,
  "org.apache.spark" %% "spark-mllib"             % sparkVersion,
  "org.apache.spark" %% "spark-streaming"         % sparkVersion,
  "org.apache.bahir" %% "spark-streaming-twitter" % "2.4.0" excludeAll ExclusionRule(organization = "org.apache.spark"),
  "edu.stanford.nlp"  % "stanford-corenlp"        % standfordNlpVersion,
  "edu.stanford.nlp"  % "stanford-corenlp"        % standfordNlpVersion classifier "models",
  "org.scalatest"    %% "scalatest"               % "3.2.7" % "test"
)
