package io.github.lazyengineer.castaway.androidApp.view.mock

import io.github.lazyengineer.castaway.androidApp.view.podcast.PodcastEpisode
import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.FeedData
import io.github.lazyengineer.castaway.domain.entity.FeedInfo
import io.github.lazyengineer.castaway.domain.entity.PlaybackPosition

object MockData {

  private const val FEED_URL = "feed.url.com"
  private const val FEED_TITLE = "castaway"
  private const val FEED_IMAGE_URL = "feed.image.com"

  private const val EPISODE_ID = "id"
  private const val EPISODE_TITLE = "episode"
  private const val EPISODE_SUB_TITLE = "sub"
  private const val EPISODE_DESCRIPTION = "description"
  private const val EPISODE_AUDIO_URL = "episode.audio.com"
  private const val EPISODE_IMAGE_URL = "episode.image.com"
  private const val EPISODE_AUTHOR = "author"
  private const val EPISODE_PLAYBACK_POSITION = 0L
  private const val EPISODE_PLAYBACK_DURATION = 100_000L
  private const val EPISODE_NUMBER = 1
  private const val EPISODE_PODCAST_URL = "feed.url.com"

  private const val EPISODE_PLAYBACK_SPEED = 1f
  private const val EPISODE_PLAYING = false
  private const val EPISODE_BUFFERING = false

  private val EPISODE_PLAYBACK = PlaybackPosition(EPISODE_PLAYBACK_POSITION, EPISODE_PLAYBACK_DURATION)

  fun podcastEpisode() = PodcastEpisode(
    id = EPISODE_ID,
    title = EPISODE_TITLE,
    subTitle = EPISODE_SUB_TITLE,
    audioUrl = EPISODE_AUDIO_URL,
    imageUrl = EPISODE_IMAGE_URL,
    author = EPISODE_AUTHOR,
    description = EPISODE_DESCRIPTION,
    episode = EPISODE_NUMBER,
    podcastUrl = EPISODE_PODCAST_URL,
    playbackPosition = EPISODE_PLAYBACK_POSITION,
    playbackDuration = EPISODE_PLAYBACK_DURATION,
    playbackSpeed = EPISODE_PLAYBACK_SPEED,
    playing = EPISODE_PLAYING,
    buffering = EPISODE_BUFFERING,
  )

  fun episode() = Episode(
    id = EPISODE_ID,
    title = EPISODE_TITLE,
    subTitle = EPISODE_SUB_TITLE,
    description = EPISODE_DESCRIPTION,
    audioUrl = EPISODE_AUDIO_URL,
    imageUrl = EPISODE_IMAGE_URL,
    author = EPISODE_AUTHOR,
    playbackPosition = EPISODE_PLAYBACK,
    episode = EPISODE_NUMBER,
    podcastUrl = EPISODE_PODCAST_URL,
  )

  fun feedData() = FeedData(
    info = FeedInfo(
      url = FEED_URL,
      title = FEED_TITLE,
      imageUrl = FEED_IMAGE_URL
    ),
    episodes = listOf(episode())
  )
}
