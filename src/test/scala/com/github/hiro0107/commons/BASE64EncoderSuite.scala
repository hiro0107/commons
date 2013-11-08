package com.github.hiro0107.commons

import org.mockito.Mockito._
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class BASE64EncoderSuite extends FunSuite with ShouldMatchers {
  test("encodeが正常に使用できる") {
    BASE64Encoder.encode("ABCDEFG".map(_.toByte).toArray) should be ("QUJDREVGRw==")
  }
  test("ランダムなバイト配列でencode/decodeが正常に使用できる") {
    import scala.util._

    for(_ <- 0 until 100) {
      val bytes = Array[Byte](100)
      Random.nextBytes(bytes)
      val base64 = BASE64Encoder.encode(bytes)
      BASE64Decoder.decode(base64) should be (bytes)
    }
  }
}
