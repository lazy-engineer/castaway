package io.github.lazyengineer.castaway.shared

import kotlinx.cinterop.memScoped
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.toCValues
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation

actual typealias Image = UIImage

actual fun ByteArray.toNativeImage(): Image? =
  memScoped {
	toCValues()
	  .ptr
	  .let { NSData.dataWithBytes(it, size.toULong()) }
	  .let { UIImage.imageWithData(it) }
  }

actual fun UIImage.fromNativeImage(): ByteArray? =
  memScoped {
	val nsData = UIImagePNGRepresentation(this@fromNativeImage)
	val bytes = nsData?.bytes()
	bytes?.readBytes(nsData.length().toInt())
  }
