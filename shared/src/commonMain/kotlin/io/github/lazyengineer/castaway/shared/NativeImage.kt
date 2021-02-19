package io.github.lazyengineer.castaway.shared

expect class Image

expect fun ByteArray.toNativeImage(): Image?
