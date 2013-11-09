organization := "com.github.hiro0107.commons"

name := "commons"

version := "1.1"

scalaVersion := "2.10.3"

resolvers += "hiro0107 repository" at "http://dl.bintray.com/hiro0107/maven"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test",
  "org.mockito" % "mockito-all" % "1.8.5",
  "com.github.loanptn" % "scala-loan-pattern-library_2.10" % "1.1"
)

