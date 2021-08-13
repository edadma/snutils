package xyz.hyperreal.snutils

import scala.scalanative.posix.unistd.readlink
import scala.scalanative.unsafe.{fromCString, stackalloc, toCString, Zone}
import scala.scalanative.unsigned.UnsignedRichInt

object unistd {

  def readLink(path: String): Option[String] = Zone { implicit z =>
    val buf = stackalloc[Byte](1024)
    val len = readlink(toCString(path), buf, 1023.toUInt)

    if (len > -1) {
      buf(len) = '\u0000'
      Some(fromCString(buf))
    } else None
  }

}
