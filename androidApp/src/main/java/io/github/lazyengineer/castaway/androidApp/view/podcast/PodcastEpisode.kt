package io.github.lazyengineer.castaway.androidApp.view.podcast

import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.PlaybackPosition

data class PodcastEpisode(
  val id: String,
  val title: String,
  val subTitle: String?,
  val audioUrl: String,
  val imageUrl: String?,
  val author: String?,
  val description: String?,
  val episode: Int,
  val podcastUrl: String,
  val playbackPosition: Long = 0,
  val playbackDuration: Long = 0,
  val playbackProgress: Float = 0f,
  val playbackSpeed: Float = 1f,
  val playing: Boolean = false,
  val buffering: Boolean = false,
) {

  companion object {

    fun Episode.toPodcastEpisode() = PodcastEpisode(
      id = id,
      title = title,
      subTitle = subTitle,
      audioUrl = audioUrl,
      imageUrl = imageUrl,
      author = author,
      description = description,
      episode = episode,
      podcastUrl = podcastUrl,
      playbackPosition = playbackPosition.position,
      playbackDuration = playbackPosition.duration,
      playbackProgress = playbackPosition.position.toFloat() / playbackPosition.duration,
      playing = false,
    )

    fun PodcastEpisode.toEpisode() = Episode(
      id = id,
      title = title,
      subTitle = subTitle,
      audioUrl = audioUrl,
      imageUrl = imageUrl,
      author = author,
      description = description,
      episode = episode,
      podcastUrl = podcastUrl,
      playbackPosition = PlaybackPosition(
        position = playbackPosition,
        duration = playbackDuration,
      )
    )
  }
}
