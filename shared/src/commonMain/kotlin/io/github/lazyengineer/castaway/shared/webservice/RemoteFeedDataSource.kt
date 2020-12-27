package io.github.lazyengineer.castaway.shared.webservice

import io.github.lazyengineer.castaway.shared.Result

interface RemoteFeedDataSource {

	suspend fun fetchFeed(url: String): Result<String>
}
