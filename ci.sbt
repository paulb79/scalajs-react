import org.scalajs.linker.interface.Semantics

val CI = Option(System.getProperty("CI", "")).map(_.trim.toLowerCase).filter(_.nonEmpty)

ThisBuild / parallelExecution := CI.isEmpty

ThisBuild / scalacOptions ++= {
  CI match {
    case Some("full") => Seq("-Xdisable-assertions", "-Xelide-below", "OFF")
    case _            => Nil
  }
}

ThisBuild / scalaJSStage := {
  CI match {
    case Some("full") => FullOptStage
    case _            => FastOptStage
  }
}

ThisBuild / fullOptJS / scalaJSLinkerConfig ~= {
  CI match {
    case Some("full") => _.withSemantics(_.withRuntimeClassNameMapper(Semantics.RuntimeClassNameMapper.discardAll()))
    case _            => identity
  }
}
