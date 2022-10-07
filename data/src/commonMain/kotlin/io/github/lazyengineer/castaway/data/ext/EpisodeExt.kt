package io.github.lazyengineer.castaway.data.ext

import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.PlaybackPosition
import iogithublazyengineercastawaydb.EpisodeEntity

fun Episode.toEpisodeEntity(): EpisodeEntity {
  return EpisodeEntity(
	id = this.id,
	title = this.title,
	subTitle = this.subTitle,
	description = this.description,
	audioUrl = this.audioUrl,
	imageUrl = this.imageUrl,
	author = this.author,
	playbackPosition = this.playbackPosition,
	episode = this.episode.toLong(),
	podcastUrl = this.podcastUrl,
  )
}

fun EpisodeEntity.toEpisode(): Episode {
  return Episode(
	id = this.id,
	title = this.title,
	subTitle = this.subTitle,
	description = this.description,
	audioUrl = this.audioUrl,
	imageUrl = this.imageUrl,
	author = this.author,
	playbackPosition = this.playbackPosition ?: PlaybackPosition(0),
	episode = this.episode.toInt(),
	podcastUrl = this.podcastUrl,
  )
}
