package com.github.hiro0107.commons

import org.mockito.Mockito._
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class SafeInputStreamSuite extends FunSuite with ShouldMatchers {
  test("applyが正常に使用できる") {
    import java.io.ByteArrayInputStream
    val in = new SafeInputStream(new ByteArrayInputStream(Array[Byte](0x04, 0x38, 0x13)))
    for(b <- Seq[Byte](0x04, 0x38, 0x13)) 
      in.apply() should be (b)
    intercept[java.util.NoSuchElementException] {
      in.apply()
    }
  }
  test("readが正常に使用できる") {
    import java.io.ByteArrayInputStream
    val in = new SafeInputStream(new ByteArrayInputStream(Array[Byte](0x04, 0x38, 0x13)))
    for(b <- Seq[Byte](0x04, 0x38, 0x13).map(Some(_))) 
      in.read() should be (b)
    in.read() should be (None)
  }
  test("foldLeftが正常に使用できる") {
    import java.io.ByteArrayInputStream
    val arr = Array[Byte](0x04, 0x38, 0x13)
    val in = new SafeInputStream(new ByteArrayInputStream(arr))
    in.foldLeft(0){ case (sum, b) => sum + b.toInt } should be (arr.map(_.toInt).sum)
  }
}

