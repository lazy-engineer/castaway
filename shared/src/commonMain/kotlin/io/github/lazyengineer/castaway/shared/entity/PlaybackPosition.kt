package io.github.lazyengineer.castaway.shared.entity

data class PlaybackPosition(
    val position: Long,
    val duration: Long? = null,
    val percentage: Double? = duration?.let { (position.toDouble() / duration) * 100 },
)
