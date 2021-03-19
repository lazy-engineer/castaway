package io.github.lazyengineer.castaway.shared.webservice

import co.touchlab.stately.freeze
import io.github.lazyengineer.castaway.shared.common.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class FeedRemoteDataSource constructor(private val client: HttpClient) : RemoteFeedDataSource {

  init {
    freeze()
  }

  override suspend fun fetchFeed(url: String): Result<String> {
	return try {
	  val response = client.get<String>(url)
	  Result.Success(response)
	} catch (e: Exception) {
	  Result.Error(e)
	}
  }
}