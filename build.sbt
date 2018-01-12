name := "scala-akka-stream-prototype"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies += "com.typesafe.akka"          %% "akka-stream"             % "2.5.8"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging"           % "3.7.2"
libraryDependencies += "ch.qos.logback"              % "logback-classic"         % "1.2.3"

libraryDependencies += "io.prometheus"               % "simpleclient"            % "0.0.26"
libraryDependencies += "io.prometheus"               % "simpleclient_httpserver" % "0.0.26"

libraryDependencies += "org.scalatest"              %% "scalatest"       % "3.0.4" % Test
