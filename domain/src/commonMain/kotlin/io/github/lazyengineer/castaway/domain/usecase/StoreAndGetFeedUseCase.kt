package io.github.lazyengineer.castaway.domain.usecase

import io.github.lazyengineer.castaway.domain.entity.FeedData
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.parser.FeedParser
import io.github.lazyengineer.castaway.domain.repository.FeedDataSource

/**
 * At the moment kotlinx.serialization doesn't support xml parsing
 * (https://github.com/Kotlin/kotlinx.serialization/issues/188), that's why
 * we fetch remotely the xml string from shared module and parse it on each platform,
 * store the feed object locally and return it from there as a single source of truth
 */
class StoreAndGetFeedUseCase constructor(
  private val feedRepository: FeedDataSource,
  private val feedParser: FeedParser
) {

  suspend operator fun invoke(url: String): DataResult<FeedData> {
	return when (val feedXmlResult = feedRepository.fetchXml(url)) {
	  is DataResult.Success -> {
		val feedData = feedParser.parseFeed(url, feedXmlResult.data)
		feedRepository.saveFeed(feedData)
	  }
	  is DataResult.Error -> {
		feedXmlResult
	  }
	}
  }
}
