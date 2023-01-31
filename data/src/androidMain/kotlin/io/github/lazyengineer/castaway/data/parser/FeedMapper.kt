package io.github.lazyengineer.castaway.data.parser

import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.FeedData
import io.github.lazyengineer.castaway.domain.entity.FeedInfo
import io.github.lazyengineer.castaway.domain.entity.PlaybackPosition
import io.github.lazyengineer.feedparser.model.feed.AtomFeed
import io.github.lazyengineer.feedparser.model.feed.Feed
import io.github.lazyengineer.feedparser.model.feed.RSSFeed
import java.util.UUID

object FeedMapper {

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
			  subTitle = episode.iTunes?.subtitle.orEmpty(),
			  description = episode.description,
			  audioUrl = episode.media?.url.orEmpty(),
			  imageUrl = episode.mediaNamespace?.thumbnails?.firstOrNull()?.attributes?.url
				?: this.iTunes?.image?.attributes?.href.orEmpty(),
			  author = episode.author.orEmpty(),
			  playbackPosition = PlaybackPosition(),
			  episode = index,
			  podcastUrl = url,
			)
		  })
	  }

	  is AtomFeed -> {
		FeedData(
		  FeedInfo(
			url = url,
			title = this.title?.value.orEmpty(),
			imageUrl = this.icon,
		  ),
		  episodes = this.entries.mapIndexed { index, episode ->
			Episode(
			  id = UUID.randomUUID().toString(),
			  title = episode.title?.value.orEmpty(),
			  subTitle = "",
			  description = episode.summary?.value.orEmpty(),
			  audioUrl = episode.mediaNamespace?.contents?.firstOrNull()?.attributes?.url.orEmpty(),
			  imageUrl = episode.mediaNamespace?.thumbnails?.firstOrNull()?.attributes?.url
				?: this.icon.orEmpty(),
			  author = episode.authors.firstOrNull()?.uri.orEmpty(),
			  playbackPosition = PlaybackPosition(),
			  episode = index,
			  podcastUrl = url,
			)
		  })
	  }
	}
  }
}
