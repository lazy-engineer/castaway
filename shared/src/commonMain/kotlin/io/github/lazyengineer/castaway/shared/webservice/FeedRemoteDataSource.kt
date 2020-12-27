package io.github.lazyengineer.castaway.shared.webservice

import io.github.lazyengineer.castaway.shared.Result
import io.ktor.client.*
import io.ktor.client.request.*

class FeedRemoteDataSource : RemoteFeedDataSource {

    private val client = HttpClient()

    override suspend fun fetchFeed(url: String): Result<String> {
        return try {
            val response = client.get<String>(url)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}