package io.github.lazyengineer.castaway.androidApp.webservice

import io.github.lazyengineer.castaway.androidApp.common.Result
import io.github.lazyengineer.castaway.androidApp.common.Result.Error
import io.github.lazyengineer.castaway.androidApp.common.Result.Success
import io.github.lazyengineer.feedparser.FeedParser
import io.github.lazyengineer.feedparser.model.feed.Feed
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException

class FeedRemoteDataSource private constructor(private val client: OkHttpClient) : RemoteFeedDataSource {

	override suspend fun fetchFeed(url: String): Result<Feed> {
		val request: Request = Request.Builder()
			.url(url)
			.build()

		return try {
			client.newCall(request)
				.execute()
				.use { response ->
					if (response.isSuccessful) {
						val factory = XmlPullParserFactory.newInstance()
						val xmlPullParser = factory.newPullParser()

						val feed = FeedParser.parseFeed(response.body!!.string(), xmlPullParser)
						Success(feed)
					} else {
						Error(IOException(response.message))
					}
				}
		} catch (e: Exception) {
			Error(IOException("Error fetching feed", e))
		}
	}

	companion object {

		@Volatile
		private var INSTANCE: FeedRemoteDataSource? = null

		fun getInstance(client: OkHttpClient): FeedRemoteDataSource {
			return INSTANCE ?: synchronized(this) {
				INSTANCE ?: FeedRemoteDataSource(client)
					.also { INSTANCE = it }
			}
		}
	}
}