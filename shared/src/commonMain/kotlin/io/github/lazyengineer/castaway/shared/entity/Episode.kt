package io.github.lazyengineer.castaway.shared.entity

import io.github.lazyengineer.castaway.shared.Image

data class Episode(
  val id: String,
  val title: String,
  val subTitle: String?,
  val description: String?,
  val audioUrl: String,
  val imageUrl: String?,
  val image: Image?,
  val author: String?,
  val playbackPosition: PlaybackPosition,
  val episode: Int,
  val podcastUrl: String,
)
