name := "seq-spark"
organization := "me.rayyildiz"
version := "1.0"

scalaVersion := "2.11.11"

val sparkV = "2.1.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core"  % sparkV,
  "org.apache.spark" %% "spark-sql"   % sparkV,
  "org.apache.spark" %% "spark-mllib" % sparkV,
//   "org.apache.spark" %% "spark-streaming" % sparkV,
  "org.scalatest" %% "scalatest" % "3.0.3" % "test"
)
