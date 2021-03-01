package io.github.lazyengineer.castaway.shared

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.nio.ByteBuffer

actual typealias Image = Bitmap

actual fun ByteArray.toNativeImage(): Image? = BitmapFactory.decodeByteArray(this, 0, this.size)

actual fun Image.fromNativeImage(): ByteArray? = convertBitmapToByteArrayUncompressed(this)

fun convertBitmapToByteArrayUncompressed(bitmap: Bitmap): ByteArray {
  val byteBuffer: ByteBuffer = ByteBuffer.allocate(bitmap.byteCount)
  bitmap.copyPixelsToBuffer(byteBuffer)
  byteBuffer.rewind()
  return byteBuffer.array()
}