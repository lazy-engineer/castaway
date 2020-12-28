package io.github.lazyengineer.castaway.shared.webservice

import io.github.lazyengineer.castaway.shared.common.Result

interface RemoteFeedDataSource {

	suspend fun fetchFeed(url: String): Result<String>
}
