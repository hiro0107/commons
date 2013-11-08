package com.github.hiro0107.commons

import org.mockito.Mockito._
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class BASE64DecoderSuite extends FunSuite with ShouldMatchers {
  test("decodeが正常に使用できる") {
    BASE64Decoder.decode("QUJDREVGRw==") should be ("ABCDEFG".map(_.toByte).toArray)
  }
}
