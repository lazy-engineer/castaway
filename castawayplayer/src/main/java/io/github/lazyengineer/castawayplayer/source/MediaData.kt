package io.github.lazyengineer.castawayplayer.source

import android.graphics.Bitmap
import android.support.v4.media.RatingCompat

/**
 * Kotlin data class representation of MediaMetadataCompat
 * https://developer.android.com/reference/kotlin/android/support/v4/media/MediaMetadataCompat
 */
data class MediaData(
  val mediaId: String,
  val mediaUri: String,
  val displayTitle: String,
  val displaySubtitle: String = "",
  val displayIconUri: String? = null,
  val displayIcon: Bitmap? = null,
  val displayDescription: String? = null,
  val title: String? = null,
  val artist: String? = null,
  val duration: Long? = null,
  val album: String? = null,
  val author: String? = null,
  val writer: String? = null,
  val composer: String? = null,
  val compilation: String? = null,
  val date: String? = null,
  val year: Long? = null,
  val genre: String? = null,
  val trackNumber: Long? = null,
  val numTracks: Long? = null,
  val discNumber: Long? = null,
  val albumArtist: String? = null,
  val art: Bitmap? = null,
  val artUri: String? = null,
  val albumArt: Bitmap? = null,
  val albumArtUri: String? = null,
  val userRating: RatingCompat? = null,
  val rating: RatingCompat? = null,
  val btFolderType: Long? = null,
  val advertisement: Long? = null,
  val downloadStatus: Long? = null,

  val playbackPosition: Long = 0,
)
