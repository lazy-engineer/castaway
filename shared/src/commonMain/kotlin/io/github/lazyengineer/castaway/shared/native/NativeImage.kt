package io.github.lazyengineer.castaway.shared.native

expect class Image

expect fun ByteArray.toNativeImage(): Image?

expect fun Image.fromNativeImage(): ByteArray?
