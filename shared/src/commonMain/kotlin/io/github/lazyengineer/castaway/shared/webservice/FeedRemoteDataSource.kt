package io.github.lazyengineer.castaway.shared.webservice

import io.github.lazyengineer.castaway.shared.common.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FeedRemoteDataSource constructor(private val client: HttpClient, private val backgroundDispatcher: CoroutineDispatcher) : RemoteFeedDataSource {

  override suspend fun fetchFeed(url: String): Result<String> {
	return withContext(backgroundDispatcher) {
	  try {
		val response = client.get<String>(url)
		Result.Success(response)
	  } catch (e: Exception) {
		Result.Error(e)
	  }
	}
  }
}