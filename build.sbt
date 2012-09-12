name := "FWUIEngine"

version := "1.0"

scalaVersion := "2.9.1"

compileOrder := CompileOrder.JavaThenScala

libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.6.1"

libraryDependencies += "org.apache.wicket" % "wicket-core" % "1.5.3"

libraryDependencies += "org.mortbay.jetty" % "jetty" % "6.1.26"

libraryDependencies += "junit" % "junit" % "4.8.2"

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.2.0"

libraryDependencies += "org.reflections" % "reflections" % "0.9.8"

libraryDependencies += "org.neo4j" % "neo4j" % "1.7.2"