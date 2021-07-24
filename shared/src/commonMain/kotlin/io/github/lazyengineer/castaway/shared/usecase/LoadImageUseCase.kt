package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.native.Image
import io.github.lazyengineer.castaway.shared.common.UseCaseWrapper
import io.github.lazyengineer.castaway.shared.webservice.ImageLoader

class LoadImageUseCase constructor(
  private val imageLoader: ImageLoader
) {

  operator fun invoke(imageUrl: String) = UseCaseWrapper<Image, String> { imageLoader.loadImage(imageUrl) }
}
