package io.github.lazyengineer.castaway.androidApp.webservice

import io.github.lazyengineer.castaway.androidApp.common.Result
import io.github.lazyengineer.feedparser.model.feed.Feed

interface RemoteFeedDataSource {

	suspend fun fetchFeed(url: String): Result<Feed>
}
