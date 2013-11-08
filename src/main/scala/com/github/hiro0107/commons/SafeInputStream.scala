package com.github.hiro0107.commons

import java.io.InputStream

class SafeInputStream(val in: InputStream) {
  def apply(): Byte = in.read match {
    case -1 => throw new java.util.NoSuchElementException
    case c => c.toByte
  }
  def read(): Option[Byte] = in.read match {
    case -1 => None
    case c => Some(c.toByte)
  }
  def foldLeft[A](a: A)(f: (A, Byte) => A): A = in.read match {
    case -1 => a
    case c => foldLeft(f(a, c.toByte))(f)
  }
}
