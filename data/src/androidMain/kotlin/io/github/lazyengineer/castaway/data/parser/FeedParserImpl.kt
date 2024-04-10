package io.github.lazyengineer.castaway.data.parser

import io.github.lazyengineer.castaway.data.parser.FeedMapper.toFeedData
import io.github.lazyengineer.castaway.domain.entity.FeedData
import io.github.lazyengineer.castaway.domain.parser.FeedParser
import org.xmlpull.v1.XmlPullParser

class FeedParserImpl(private val parser: XmlPullParser) : FeedParser {

  override fun parseFeed(url: String, feedXml: String): FeedData {
    val feed = io.github.lazyengineer.feedparser.FeedParser.parseFeed(feedXml, parser)
    return feed.toFeedData(url)
  }
}
