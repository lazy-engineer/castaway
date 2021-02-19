package io.github.lazyengineer.castaway.shared.usecase

import io.github.lazyengineer.castaway.shared.Image
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.UseCase
import io.github.lazyengineer.castaway.shared.webservice.ImageLoader

class LoadImageUseCase constructor(
  private val imageLoader: ImageLoader
) : UseCase<Image, String>() {

  override suspend fun run(imageUrl: String): Result<Image> {
	return imageLoader.loadImage(imageUrl)
  }
}