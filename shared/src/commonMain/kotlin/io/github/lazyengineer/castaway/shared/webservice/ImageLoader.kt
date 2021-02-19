package io.github.lazyengineer.castaway.shared.webservice

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.shared.Image
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.toNativeImage
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class ImageLoader constructor(private val client: HttpClient) {

  init {
	ensureNeverFrozen()
  }

  suspend fun loadImage(url: String): Result<Image> {
	return try {
	  val response = client.get<ByteArray>(url).toNativeImage()

	  when {
		response != null -> Result.Success(response)
		else -> Result.Error(Exception("Failed to translate image to native"))
	  }
	} catch (e: Exception) {
	  Result.Error(e)
	}
  }
}