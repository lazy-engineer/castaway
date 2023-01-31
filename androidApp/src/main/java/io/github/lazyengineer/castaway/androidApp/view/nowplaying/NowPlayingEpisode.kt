package io.github.lazyengineer.castaway.androidApp.view.nowplaying

import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingPosition.Companion.toNowPlayingPosition
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingPosition.Companion.toPlaybackPosition
import io.github.lazyengineer.castaway.domain.entity.Episode

data class NowPlayingEpisode(
  val id: String,
  val title: String,
  val subTitle: String?,
  val audioUrl: String,
  val imageUrl: String?,
  val author: String?,
  val description: String?,
  val episode: Int,
  val podcastUrl: String,
  val playbackPosition: NowPlayingPosition,
) {

  companion object {

	fun Episode.toNowPlayingEpisode() = NowPlayingEpisode(
	  id = id,
	  title = title,
	  subTitle = subTitle,
	  audioUrl = audioUrl,
	  imageUrl = imageUrl,
	  author = author,
	  description = description,
	  episode = episode,
	  podcastUrl = podcastUrl,
	  playbackPosition = playbackPosition.toNowPlayingPosition(),
	)

	fun NowPlayingEpisode.toEpisode() = Episode(
	  id = id,
	  title = title,
	  subTitle = subTitle,
	  audioUrl = audioUrl,
	  imageUrl = imageUrl,
	  author = author,
	  description = description,
	  episode = episode,
	  podcastUrl = podcastUrl,
	  playbackPosition = playbackPosition.toPlaybackPosition(),
	)
  }
}
