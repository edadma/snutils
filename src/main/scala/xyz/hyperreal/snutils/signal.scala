package xyz.hyperreal.snutils

import scala.scalanative.unsafe.{extern, name, CInt}

@extern
object signal {
  def kill(pid: CInt, sig: CInt): CInt = extern

  @name("scalanative_sigkill")
  def SIGKILL: CInt = extern
}
