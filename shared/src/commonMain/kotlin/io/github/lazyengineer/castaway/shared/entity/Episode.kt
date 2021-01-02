package io.github.lazyengineer.castaway.shared.entity

data class Episode(
	val id: String,
	val title: String,
	val subTitle: String?,
	val description: String?,
	val audioUrl: String,
	val imageUrl: String?,
	val author: String?,
	val playbackPosition: PlaybackPosition,
	val isPlaying: Boolean = false,
	val podcastUrl: String,
)
