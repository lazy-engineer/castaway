package io.github.lazyengineer.castaway.androidApp.entity

import io.github.lazyengineer.castaway.androidApp.common.ArgumentsKt
import kotlinx.android.parcel.Parcelize

@Parcelize
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
) : ArgumentsKt
