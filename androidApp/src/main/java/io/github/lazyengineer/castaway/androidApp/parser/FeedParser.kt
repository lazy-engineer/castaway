package io.github.lazyengineer.castaway.androidApp.parser

import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition
import io.github.lazyengineer.feedparser.model.feed.AtomFeed
import io.github.lazyengineer.feedparser.model.feed.Feed
import io.github.lazyengineer.feedparser.model.feed.RSSFeed
import java.util.UUID

object FeedParser {

  fun Feed.toFeedData(url: String): FeedData {
	return when (this) {
		is RSSFeed -> {
			FeedData(
				url = url,
				title = this.title,
			  	image = this.iTunes?.image?.attributes?.href,
				episodes = this.items.map {
					Episode(
						id = UUID.randomUUID().toString(),
						title = it.title,
						subTitle = it.iTunes?.subtitle ?: "",
						description = it.description,
						audioUrl = it.media?.url ?: "",
						imageUrl = it.mediaNamespace?.thumbnails?.firstOrNull()?.attributes?.url
							?: "",
						author = it.author ?: "",
						playbackPosition = PlaybackPosition(0, Long.MAX_VALUE),
						podcastUrl = url,
					)
				})
		}
		is AtomFeed -> {
			FeedData(
				url = url,
				title = this.title?.value ?: "",
			  	image = this.icon,
				episodes = this.entries.map {
					Episode(
						id = UUID.randomUUID().toString(),
						title = it.title?.value ?: "",
						subTitle = "",
						description = it.summary?.value ?: "",
						audioUrl = it.mediaNamespace?.contents?.firstOrNull()?.attributes?.url
							?: "",
						imageUrl = it.mediaNamespace?.thumbnails?.firstOrNull()?.attributes?.url
							?: "",
						author = it.authors.firstOrNull()?.uri ?: "",
						playbackPosition = PlaybackPosition(0, Long.MAX_VALUE),
						podcastUrl = url,
					)
				})
		}
	}
  }
}