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
		  imageUrl = this.iTunes?.image?.attributes?.href,
		  image = null,
		  episodes = this.items.mapIndexed { index, episode ->
			Episode(
			  id = UUID.randomUUID().toString(),
			  title = episode.title,
			  subTitle = episode.iTunes?.subtitle ?: "",
			  description = episode.description,
			  audioUrl = episode.media?.url ?: "",
			  imageUrl = episode.mediaNamespace?.thumbnails?.firstOrNull()?.attributes?.url ?: "",
			  image = null,
			  author = episode.author ?: "",
			  playbackPosition = PlaybackPosition(0, Long.MAX_VALUE),
			  episode = index,
			  podcastUrl = url,
			)
		  })
	  }
	  is AtomFeed -> {
		FeedData(
		  url = url,
		  title = this.title?.value ?: "",
		  imageUrl = this.icon,
		  image = null,
		  episodes = this.entries.mapIndexed { index, episode ->
			Episode(
			  id = UUID.randomUUID().toString(),
			  title = episode.title?.value ?: "",
			  subTitle = "",
			  description = episode.summary?.value ?: "",
			  audioUrl = episode.mediaNamespace?.contents?.firstOrNull()?.attributes?.url ?: "",
			  imageUrl = episode.mediaNamespace?.thumbnails?.firstOrNull()?.attributes?.url ?: "",
			  image = null,
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