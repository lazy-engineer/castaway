package io.github.lazyengineer.castaway.androidApp.entity

import io.github.lazyengineer.castaway.androidApp.ArgumentsKt
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaybackPosition(
	val position: Long,
	val duration: Long? = null,
	val percentage: Double? = duration?.let { (position.toDouble() / duration) * 100 },
) : ArgumentsKt
