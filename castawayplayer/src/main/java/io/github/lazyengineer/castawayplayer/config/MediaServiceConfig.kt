package io.github.lazyengineer.castawayplayer.config

import coil.ImageLoader
import com.google.gson.Gson
import io.github.lazyengineer.castawayplayer.R

data class MediaServiceConfig(
	val notificationIconResId: Int = R.drawable.ic_notification,
	val playbackSpeed: Float = 1f,
	val fastForwardInterval: Long = 30_000,
	val rewindInterval: Long = 10_000,
	val positionUpdateIntervalMillis: Long = 100L,

	val gson: Gson? = null,
	val imageLoader: ImageLoader? = null,
)
