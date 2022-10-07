package io.github.lazyengineer.castaway.data.webservice

import io.github.lazyengineer.castaway.domain.entity.common.DataResult

interface RemoteFeedDataSource {

  suspend fun fetchFeed(url: String): DataResult<String>
}
