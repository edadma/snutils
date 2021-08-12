package xyz.hyperreal.snutils

import scala.scalanative.libc.stdlib.free
import scala.scalanative.unsafe.{extern, fromCString, stackalloc, toCString, CInt, CSize, CString, Ptr, Zone}

@extern
object Glob {
  type GlobT = Ptr[Byte]

  def globHelper(pattern: CString, globbuf: Ptr[GlobT], pathc: Ptr[CSize], pathv: Ptr[Ptr[CString]]): CInt = extern

  def globfree(pglob: GlobT): Unit = extern
}

object Globbing {
  import Glob._

  def expand(pattern: String): List[String] = Zone { implicit z =>
    val globbuf = stackalloc[GlobT]
    val pathc   = stackalloc[CSize]
    val pathv   = stackalloc[Ptr[CString]]

    if (globHelper(toCString(pattern), globbuf, pathc, pathv) == 0) {
      val list =
        for (i <- 0 until (!pathc).toInt)
          yield {
            val array = !pathv

            fromCString(array(i))
          }

      val pglob = !globbuf

      globfree(pglob)
      free(pglob)
      list.toList
    } else List(pattern)
  }
}
