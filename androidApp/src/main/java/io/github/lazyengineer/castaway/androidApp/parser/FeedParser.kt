package io.github.lazyengineer.castaway.androidApp.parser

import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.FeedInfo
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
		  FeedInfo(
			url = url,
			title = this.title,
			imageUrl = this.iTunes?.image?.attributes?.href,
		  ),
		  episodes = this.items.mapIndexed { index, episode ->
			Episode(
			  id = UUID.randomUUID().toString(),
			  title = episode.title,
			  subTitle = episode.iTunes?.subtitle ?: "",
			  description = episode.description,
			  audioUrl = episode.media?.url ?: "",
			  imageUrl = episode.mediaNamespace?.thumbnails?.firstOrNull()?.attributes?.url ?: this.iTunes?.image?.attributes?.href ?: "",
			  author = episode.author ?: "",
			  playbackPosition = PlaybackPosition(0, Long.MAX_VALUE),
			  episode = index,
			  podcastUrl = url,
			)
		  })
	  }
	  is AtomFeed -> {
		FeedData(
		  FeedInfo(
			url = url,
			title = this.title?.value ?: "",
			imageUrl = this.icon,
		  ),
		  episodes = this.entries.mapIndexed { index, episode ->
			Episode(
			  id = UUID.randomUUID().toString(),
			  title = episode.title?.value ?: "",
			  subTitle = "",
			  description = episode.summary?.value ?: "",
			  audioUrl = episode.mediaNamespace?.contents?.firstOrNull()?.attributes?.url ?: "",
			  imageUrl = episode.mediaNamespace?.thumbnails?.firstOrNull()?.attributes?.url ?: this.icon ?: "",
			  author = episode.authors.firstOrNull()?.uri ?: "",
			  playbackPosition = PlaybackPosition(0, Long.MAX_VALUE),
			  episode = index,
			  podcastUrl = url,
			)
		  })
	  }
	}
  }
}
