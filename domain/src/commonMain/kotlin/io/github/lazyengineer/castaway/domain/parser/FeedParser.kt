package io.github.lazyengineer.castaway.domain.parser

import io.github.lazyengineer.castaway.domain.entity.FeedData

interface FeedParser {

  fun parseFeed(url: String, feedXml: String): FeedData
}
