package io.github.lazyengineer.castawayplayer.extention

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.RatingCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import io.github.lazyengineer.castawayplayer.service.Constants.MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS
import io.github.lazyengineer.castawayplayer.source.MediaData
import android.support.v4.media.MediaBrowserCompat.MediaItem as MediaItemCompat

inline val MediaMetadataCompat.id: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

inline val MediaMetadataCompat.title: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_TITLE)

inline val MediaMetadataCompat.artist: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_ARTIST)

inline val MediaMetadataCompat.duration
  get() = getLong(MediaMetadataCompat.METADATA_KEY_DURATION)

inline val MediaMetadataCompat.album: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM)

inline val MediaMetadataCompat.author: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_AUTHOR)

inline val MediaMetadataCompat.writer: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_WRITER)

inline val MediaMetadataCompat.composer: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_COMPOSER)

inline val MediaMetadataCompat.compilation: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_COMPILATION)

inline val MediaMetadataCompat.date: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_DATE)

inline val MediaMetadataCompat.year: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_YEAR)

inline val MediaMetadataCompat.genre: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_GENRE)

inline val MediaMetadataCompat.trackNumber
  get() = getLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER)

inline val MediaMetadataCompat.trackCount
  get() = getLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS)

inline val MediaMetadataCompat.discNumber
  get() = getLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER)

inline val MediaMetadataCompat.albumArtist: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST)

inline val MediaMetadataCompat.art: Bitmap?
  get() = getBitmap(MediaMetadataCompat.METADATA_KEY_ART)

inline val MediaMetadataCompat.artUri: Uri
  get() = this.getString(MediaMetadataCompat.METADATA_KEY_ART_URI).toUri()

inline val MediaMetadataCompat.albumArt: Bitmap?
  get() = getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)

inline val MediaMetadataCompat.albumArtUri: Uri
  get() = this.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI).toUri()

inline val MediaMetadataCompat.userRating: RatingCompat?
  get() = getRating(MediaMetadataCompat.METADATA_KEY_USER_RATING)

inline val MediaMetadataCompat.rating: RatingCompat?
  get() = getRating(MediaMetadataCompat.METADATA_KEY_RATING)

inline val MediaMetadataCompat.displayTitle: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE)

inline val MediaMetadataCompat.displaySubtitle: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE)

inline val MediaMetadataCompat.displayDescription: String?
  get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION)

inline val MediaMetadataCompat.displayIcon: Bitmap?
  get() = getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON)

inline val MediaMetadataCompat.displayIconUri: Uri
  get() = this.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI).toUri()

inline val MediaMetadataCompat.mediaUri: Uri
  get() = this.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri()

inline val MediaMetadataCompat.btFolderType
  get() = getLong(MediaMetadataCompat.METADATA_KEY_BT_FOLDER_TYPE)

inline val MediaMetadataCompat.advertisement
  get() = getLong(MediaMetadataCompat.METADATA_KEY_ADVERTISEMENT)

inline val MediaMetadataCompat.downloadStatus
  get() = getLong(MediaMetadataCompat.METADATA_KEY_DOWNLOAD_STATUS)

/**
 * Custom property for storing whether a [MediaMetadataCompat] item represents an
 * item that is [MediaItem.FLAG_BROWSABLE] or [MediaItem.FLAG_PLAYABLE].
 */
inline val MediaMetadataCompat.flag
  get() = this.getLong(METADATA_KEY_MEDIA_FLAGS).toInt()

/**
 * Useful extensions for [MediaMetadataCompat.Builder].
 */

// These do not have getters, so create a message for the error.
const val NO_GET = "Property does not have a 'get'"

inline var MediaMetadataCompat.Builder.id: String
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, value)
  }

inline var MediaMetadataCompat.Builder.title: String?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_TITLE, value)
  }

inline var MediaMetadataCompat.Builder.artist: String?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_ARTIST, value)
  }

inline var MediaMetadataCompat.Builder.album: String?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_ALBUM, value)
  }

inline var MediaMetadataCompat.Builder.duration: Long
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putLong(MediaMetadataCompat.METADATA_KEY_DURATION, value)
  }

inline var MediaMetadataCompat.Builder.genre: String?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_GENRE, value)
  }

inline var MediaMetadataCompat.Builder.mediaUri: String?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, value)
  }

inline var MediaMetadataCompat.Builder.albumArtUri: String?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, value)
  }

inline var MediaMetadataCompat.Builder.albumArt: Bitmap?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, value)
  }

inline var MediaMetadataCompat.Builder.trackNumber: Long
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, value)
  }

inline var MediaMetadataCompat.Builder.trackCount: Long
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, value)
  }

inline var MediaMetadataCompat.Builder.displayTitle: String?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, value)
  }

inline var MediaMetadataCompat.Builder.displaySubtitle: String?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, value)
  }

inline var MediaMetadataCompat.Builder.displayDescription: String?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, value)
  }

inline var MediaMetadataCompat.Builder.displayIconUri: String?
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, value)
  }

inline var MediaMetadataCompat.Builder.downloadStatus: Long
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putLong(MediaMetadataCompat.METADATA_KEY_DOWNLOAD_STATUS, value)
  }

/**
 * Custom property for storing whether a [MediaMetadataCompat] item represents an
 * item that is [MediaItem.FLAG_BROWSABLE] or [MediaItem.FLAG_PLAYABLE].
 */
inline var MediaMetadataCompat.Builder.flag: Int
  @Deprecated(NO_GET, level = DeprecationLevel.ERROR)
  get() = throw IllegalAccessException("Cannot get from MediaMetadataCompat.Builder")
  set(value) {
	putLong(METADATA_KEY_MEDIA_FLAGS, value.toLong())
  }

/**
 * Extension method for building an [ExtractorMediaSource] from a [MediaMetadataCompat] object.
 *
 * For convenience, place the [MediaDescriptionCompat] into the tag so it can be retrieved later.
 */
fun MediaMetadataCompat.toMediaSource(dataSourceFactory: DataSource.Factory) =
  ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(mediaUri))

/**
 * Extension method for building a [ConcatenatingMediaSource] given a [List]
 * of [MediaMetadataCompat] objects.
 */
fun List<MediaMetadataCompat>.toMediaSource(
  dataSourceFactory: DataSource.Factory
): ConcatenatingMediaSource {

  val concatenatingMediaSource = ConcatenatingMediaSource()
  forEach {
	concatenatingMediaSource.addMediaSource(it.toMediaSource(dataSourceFactory))
  }
  return concatenatingMediaSource
}

fun MediaData.asMediaMetadata(): MediaMetadataCompat = let { media ->
  val builder = MediaMetadataCompat.Builder()

  builder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, media.mediaId)
  builder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, media.mediaUri)
  builder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, media.displayTitle)
  builder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, media.displaySubtitle)

  media.displayIcon?.let { builder.putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, it) }
  media.displayIconUri?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, it) }
  media.displayDescription?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, it) }
  media.title?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, it) }
  media.artist?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, it) }
  media.duration?.let { builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, it) }
  media.album?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, it) }
  media.author?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, it) }
  media.writer?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_WRITER, it) }
  media.composer?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_COMPOSER, it) }
  media.compilation?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_COMPILATION, it) }
  media.date?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_DATE, it) }
  media.year?.let { builder.putLong(MediaMetadataCompat.METADATA_KEY_YEAR, it) }
  media.genre?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_GENRE, it) }
  media.trackNumber?.let { builder.putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, it) }
  media.numTracks?.let { builder.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, it) }
  media.discNumber?.let { builder.putLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, it) }
  media.albumArtist?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, it) }
  media.art?.let { builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, it) }
  media.artUri?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_ART_URI, it) }
  media.albumArt?.let { builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, it) }
  media.albumArtUri?.let { builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, it) }
  media.userRating?.let { builder.putRating(MediaMetadataCompat.METADATA_KEY_USER_RATING, it) }
  media.rating?.let { builder.putRating(MediaMetadataCompat.METADATA_KEY_RATING, it) }
  media.btFolderType?.let { builder.putLong(MediaMetadataCompat.METADATA_KEY_BT_FOLDER_TYPE, it) }
  media.advertisement?.let { builder.putLong(MediaMetadataCompat.METADATA_KEY_ADVERTISEMENT, it) }
  media.downloadStatus?.let { builder.putLong(MediaMetadataCompat.METADATA_KEY_DOWNLOAD_STATUS, it) }

  builder.build()
}

fun MediaData.asMediaItem(): MediaItemCompat = MediaItemCompat(this.asMediaDescription(), MediaItemCompat.FLAG_PLAYABLE)

fun MediaData.asMediaDescription(): MediaDescriptionCompat = let { mediaData ->
  val extras = Bundle().also {
	it.putLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, mediaData.playbackPosition ?: C.TIME_UNSET)
  }

  MediaDescriptionCompat.Builder()
	.setMediaId(mediaData.mediaId)
	.setTitle(mediaData.displayTitle)
	.setSubtitle(mediaData.displaySubtitle)
	.setDescription(mediaData.displayDescription)
	.setIconUri(Uri.parse(mediaData.displayIconUri))
	.setMediaUri(Uri.parse(mediaData.mediaUri))
	.setExtras(extras)
	.build()
}

fun MediaMetadataCompat.asMediaData() = with(this) {
  MediaData(
	mediaId = id.orEmpty(),
	mediaUri = mediaUri.toString(),
	displayTitle = displayTitle.orEmpty(),
	displaySubtitle = displaySubtitle.orEmpty(),
	displayIconUri = displayIconUri.toString(),
	displayIcon = displayIcon,
	displayDescription = displayDescription,
	title = title,
	artist = artist,
	duration = duration,
	album = album,
	author = author,
	writer = writer,
	composer = composer,
	compilation = compilation,
	date = date,
	year = year?.toLong(),
	genre = genre,
	trackNumber = trackNumber,
	numTracks = trackCount,
	discNumber = discNumber,
	albumArtist = albumArtist,
	art = art,
	artUri = artUri.toString(),
	albumArt = albumArt,
	albumArtUri = albumArtUri.toString(),
	userRating = userRating,
	rating = rating,
	btFolderType = btFolderType,
	advertisement = advertisement,
	downloadStatus = downloadStatus,
  )
}

/**
 * Custom property that holds whether an item is [MediaItem.FLAG_BROWSABLE] or
 * [MediaItem.FLAG_PLAYABLE].
 */
const val METADATA_KEY_MEDIA_FLAGS = "io.github.lazyengineer.podcaster.METADATA_KEY_MEDIA_FLAGS"

/**
 * Helper extension to convert a potentially null [String] to a [Uri] falling back to [Uri.EMPTY]
 */
fun String?.toUri(): Uri = this?.let { Uri.parse(it) } ?: Uri.EMPTY
