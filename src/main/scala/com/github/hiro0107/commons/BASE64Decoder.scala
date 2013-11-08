package com.github.hiro0107.commons

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * A BASE64 decoder. BASE64 is defined in RFC 1521.
 */
object BASE64Decoder {
  import BASE64._

  private val rindices: Array[Byte] = {
    val bytes = (for(i <- 0 until 256) yield((-1).toByte)).toArray
    for(i <- 0 until 64) { bytes(indices(i)) = i.toByte }
    bytes
  }

  /**
   * 指定した文字列をデコードする。
   */
  def decode(s: String): Array[Byte] = {
    val is: ByteArrayInputStream = new ByteArrayInputStream(s.getBytes)
    val os: ByteArrayOutputStream = new ByteArrayOutputStream
    try {
      decode(is, os)
    }
    catch {
      case e: IOException => {
        throw new Error("BASE64Decoder: internal error")
      }
    }
    os.toByteArray
  }

  /**
   * 入力ストリーム <code>is</code> を読み込んでデコードし、出力ストリーム <code>os</code>にデコード結果を出力する。
   */
  def decode(is: InputStream, os: OutputStream) {
    val buf: Array[Byte] = new Array[Byte](4)
    val in = new SafeInputStream(is)
    def loop(i: Int = 0, len: Int = 0): (Int, Int) = {
      in.read() match {
        case Some(c) =>
          val b: Byte = rindices(c & 0xff)
          val (i2, len2) = if (b != -1.asInstanceOf[Byte]) {
            buf(i) = b
            if (c != '=')
              (i + 1, i + 1)
            else
              (i + 1, len)
          } else (i, len)
          if (i2 == 4) {
            decodeSub(os, buf, len2)
            loop(0, 0)
          } else
            loop(i2, len2)
        case None => (i, len)
      }
    }
    val (i, len) = loop()
    if (i > 0) {
      decodeSub(os, buf, len)
    }
  }

  /**
   * <code>decode</code> メソッドから呼び出されるサブルーチン。
   */
  private def decodeSub(os: OutputStream, buf: Array[Byte], len: Int) {
    val buf2: Array[Byte] = new Array[Byte](3)
    buf2(0) = ((buf(0) << 2) | ((buf(1) & 0x30) >> 4)).asInstanceOf[Byte]
    buf2(1) = (((buf(1) & 0x0f) << 4) | ((buf(2) & 0x3c) >> 2)).asInstanceOf[Byte]
    buf2(2) = (((buf(2) & 0x03) << 6) | (buf(3) & 0x3f)).asInstanceOf[Byte]
    
    os.write(buf2, 0, len match {
      case 4 => 3
      case 3 => 2
      case 2 => 1
    })
  }

}