package io.github.lazyengineer.castaway.shared

import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage

actual typealias Image = UIImage

actual fun ByteArray.toNativeImage(): Image? =
  memScoped {
    toCValues()
      .ptr
      .let { NSData.dataWithBytes(it, size.toULong()) }
      .let { UIImage.imageWithData(it) }
  }

//TODO: implement UIImage to ByteArray converter
actual fun UIImage.fromNativeImage(): ByteArray? = null
