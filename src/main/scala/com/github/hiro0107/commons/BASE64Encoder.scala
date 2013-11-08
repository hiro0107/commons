package com.github.hiro0107.commons

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * A BASE64 encoder. BASE64 is defined in RFC 1521.
 *
 */
object BASE64Encoder {
  import BASE64._
  /**
   * 指定したバッファをエンコードする。
   *
   * @return エンコード結果を返す。
   */
  def encode(buf: Array[Byte]): String = {
    val is: ByteArrayInputStream = new ByteArrayInputStream(buf)
    val os: ByteArrayOutputStream = new ByteArrayOutputStream
    try {
      encode(is, os)
    }
    catch {
      case e: IOException => {
        throw new Error("BASE64Encoder: internal error")
      }
    }
    os.toString
  }

  /**
   * 指定したファイルを読みこみ、その内容をエンコードする。
   *
   * @return エンコード結果を返す。
   */
  def encodeFromFile(pathname: String): String = {
    import com.github.loanptn._

    using(new FileInputStream(pathname)) { is =>
      val os = new ByteArrayOutputStream
      encode(is, os)
      os.toString
    }
  }

  /**
   * 入力ストリーム <code>is</code> を読み込んでエンコードし、出力ストリーム <code>os</code>
   * にエンコード結果を出力する。
   */
  def encode(is: InputStream, os: OutputStream) {
    val buf: Array[Byte] = new Array[Byte](3)
    def loop(count: Int = 0, n: Int = 0) {
      val count2 = is.read(buf)
      if (count2 > 0) {
        encodeItem(os, buf, count2)
      }
      val n2 = n + 4
      val n3 = if (n2 == 76) {
        os.write('\n')
        0
      } else n2
      if(count2 == 3) loop(count2, n3)
    }
    loop()
  }

  /**
   * ３バイトの入力データから、４文字の印刷可能な文字列を生成する。
   */
  private def encodeItem(os: OutputStream, buf: Array[Byte], count: Int) {
    os.write(BASE64.indices((buf(0) & 0xFC) >> 2))
    count match {
      case 1 =>
        os.write(indices((buf(0) & 0x3) << 4))
        os.write('=')
        os.write('=')
      case 2 =>
        os.write(indices(((buf(0) & 0x3) << 4) | ((buf(1) & 0xF0) >> 4)))
        os.write(indices((buf(1) & 0xF) << 2))
        os.write('=')
      case 3 =>
        os.write(indices(((buf(0) & 0x3) << 4) | ((buf(1) & 0xF0) >> 4)))
        os.write(indices(((buf(1) & 0xF) << 2) | ((buf(2) & 0xC0) >> 6)))
        os.write(indices(buf(2) & 0x3F))
    }
  }
}