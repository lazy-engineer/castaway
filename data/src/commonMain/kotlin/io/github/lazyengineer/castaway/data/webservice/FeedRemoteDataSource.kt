package io.github.lazyengineer.castaway.data.webservice

import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.entity.common.DataResult.Success
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class FeedRemoteDataSource(private val client: HttpClient) : RemoteFeedDataSource {

  override suspend fun fetchFeed(url: String): DataResult<String> {
	return try {
	  val response = client.get(url).body<String>()
	  Success(response)
	} catch (e: Exception) {
	  DataResult.Error(e)
	}
  }
}
