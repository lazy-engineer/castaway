package io.github.lazyengineer.castaway.androidApp.usecase

import io.github.lazyengineer.castaway.androidApp.parser.FeedParser.toFeedData
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.UseCaseWrapper
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.repository.FeedDataSource
import io.github.lazyengineer.feedparser.FeedParser
import org.xmlpull.v1.XmlPullParserFactory

/**
 * At the moment kotlinx.serialization doesn't support xml parsing
 * (https://github.com/Kotlin/kotlinx.serialization/issues/188), that's why
 * we fetch remotely the xml string from shared module and parse it on each platform,
 * store the feed object locally and return it from there as a single source of truth
 */
class StoreAndGetFeedUseCase constructor(
  private val feedRepository: FeedDataSource
) {

  operator fun invoke(url: String): UseCaseWrapper<FeedData, String> {
	return UseCaseWrapper {
	  when (val feedXmlResult = feedRepository.fetchXml(url)) {
		is Result.Success -> {
		  val factory = XmlPullParserFactory.newInstance()
		  val xmlPullParser = factory.newPullParser()
		  val feed = FeedParser.parseFeed(feedXmlResult.data, xmlPullParser)
		  val feedData = feed.toFeedData(url)

		  feedRepository.saveFeed(feedData)
		}
		is Result.Error -> {
		  feedXmlResult
		}
	  }
	}
  }
}