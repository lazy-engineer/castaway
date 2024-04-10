package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Arc
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.Transition
import androidx.constraintlayout.compose.Visibility
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.FastForward
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.PlayPause
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.Rewind
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingPosition
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component.EpisodeImage
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component.EpisodeTitle
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component.ForwardButton
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component.PlayButton
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component.PlaybackProgress
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component.PlaybackSpeed
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component.PlaylistIcon
import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component.RewindButton
import io.github.lazyengineer.castaway.domain.resource.ThemeType.MATERIAL

@Composable
internal fun NowPlayingMotionView(
  @FloatRange(from = 0.0, to = 1.0) expandedPercentage: () -> Float,
  episode: () -> NowPlayingEpisode,
  playing: () -> Boolean,
  playbackSpeed: () -> Float,
  modifier: Modifier = Modifier,
  event: (NowPlayingEvent) -> Unit
) {
  val episodeImageId = "episode_image"
  val playlistIconId = "playlist_icon"
  val rewindButtonId = "rewind_button"
  val forwardButtonId = "forward_button"
  val playButtonId = "play_button"
  val episodeTitleId = "episode_title"
  val playbackProgressId = "playback_progress"
  val playbackSpeedId = "playback_speed"

  MotionLayout(
    motionScene = rememberMotionScene(
      episodeImageId = episodeImageId,
      playlistIconId = playlistIconId,
      rewindButtonId = rewindButtonId,
      forwardButtonId = forwardButtonId,
      playButtonId = playButtonId,
      episodeTitleId = episodeTitleId,
      playbackProgressId = playbackProgressId,
      playbackSpeedId = playbackSpeedId
    ),
    progress = expandedPercentage(),
    modifier = modifier
      .fillMaxWidth()
      .background(CastawayTheme.colors.background)
  ) {
    EpisodeImage(
      imageUrl = episode().imageUrl,
      modifier = Modifier
        .clip(RoundedCornerShape(25f))
        .layoutId(episodeImageId)
    )

    EpisodeTitle(
      episodeTitle = episode().title,
      modifier = Modifier.layoutId(episodeTitleId)
    )

    RewindButton(
      modifier = Modifier.layoutId(rewindButtonId)
    ) {
      event(Rewind)
    }

    PlayButton(
      playing = playing(),
      modifier = Modifier.layoutId(playButtonId)
    ) {
      event(PlayPause(episode().id))
    }

    ForwardButton(
      modifier = Modifier.layoutId(forwardButtonId)
    ) {
      event(FastForward)
    }

    PlaybackProgress(
      expandedPercentage = expandedPercentage,
      playbackPosition = { episode().playbackPosition },
      event = remember { event },
      modifier = Modifier.layoutId(playbackProgressId)
    )

    PlaylistIcon(
      modifier = Modifier.layoutId(playlistIconId)
    ) {}

    PlaybackSpeed(
      playbackSpeed = playbackSpeed(),
      event = event,
      modifier = Modifier.layoutId(playbackSpeedId),
    )
  }
}

@Composable
private fun rememberMotionScene(
  episodeImageId: String,
  playlistIconId: String,
  rewindButtonId: String,
  forwardButtonId: String,
  playButtonId: String,
  episodeTitleId: String,
  playbackProgressId: String,
  playbackSpeedId: String,
  startConstraintId: String = "start",
  endConstraintId: String = "end",
): MotionScene {

  val startSet = startConstraintSet(
    episodeImageId = episodeImageId,
    playlistIconId = playlistIconId,
    playButtonId = playButtonId,
    rewindButtonId = rewindButtonId,
    forwardButtonId = forwardButtonId,
    episodeTitleId = episodeTitleId,
    playbackProgressId = playbackProgressId,
    playbackSpeedId = playbackSpeedId
  )

  val endSet = endConstraintSet(
    episodeImageId = episodeImageId,
    playlistIconId = playlistIconId,
    playButtonId = playButtonId,
    rewindButtonId = rewindButtonId,
    forwardButtonId = forwardButtonId,
    episodeTitleId = episodeTitleId,
    playbackProgressId = playbackProgressId,
    playbackSpeedId = playbackSpeedId
  )

  val transition = Transition(from = startConstraintId, to = endConstraintId) {
    motionArc = Arc.StartHorizontal

    keyAttributes(ConstrainedLayoutReference(episodeTitleId)) {
      frame(75) {
        this.alpha = 0f
      }
      frame(100) {
        this.alpha = 1f
      }
    }

    keyAttributes(ConstrainedLayoutReference(playbackSpeedId)) {
      frame(75) {
        this.alpha = 0f
      }
      frame(100) {
        this.alpha = 1f
      }
    }
  }

  return remember {
    MotionScene {
      addConstraintSet(name = startConstraintId, constraintSet = startSet)
      addConstraintSet(name = endConstraintId, constraintSet = endSet)
      addTransition(name = "default", transition = transition)
    }
  }
}

@Composable
private fun startConstraintSet(
  episodeImageId: String,
  playlistIconId: String,
  playButtonId: String,
  rewindButtonId: String,
  forwardButtonId: String,
  episodeTitleId: String,
  playbackProgressId: String,
  playbackSpeedId: String
) = ConstraintSet {
  val episodeImage = createRefFor(episodeImageId)
  val playlistIcon = createRefFor(playlistIconId)
  val playButton = createRefFor(playButtonId)
  val rewindButton = createRefFor(rewindButtonId)
  val forwardButton = createRefFor(forwardButtonId)
  val episodeTitle = createRefFor(episodeTitleId)
  val playbackProgress = createRefFor(playbackProgressId)
  val playbackSpeed = createRefFor(playbackSpeedId)

  createHorizontalChain(
    episodeImage,
    rewindButton,
    playButton,
    forwardButton,
    playlistIcon.withChainParams(startMargin = 16.dp, endMargin = 8.dp),
    chainStyle = ChainStyle.SpreadInside
  )

  constrain(episodeImage) {
    width = Dimension.value(56.dp)
    height = Dimension.value(56.dp)
    top.linkTo(parent.top)
  }

  constrain(episodeTitle) {
    visibility = Visibility.Invisible
    top.linkTo(parent.top)
    start.linkTo(parent.start, margin = 24.dp)
    end.linkTo(parent.end, margin = 24.dp)
  }

  constrain(playButton) {
    width = Dimension.value(48.dp)
    height = Dimension.value(48.dp)
    start.linkTo(rewindButton.end)
    top.linkTo(episodeImage.top)
    bottom.linkTo(episodeImage.bottom)
  }

  constrain(rewindButton) {
    width = Dimension.value(36.dp)
    height = Dimension.value(36.dp)
    start.linkTo(episodeImage.end)
    top.linkTo(episodeImage.top)
    bottom.linkTo(episodeImage.bottom)
  }

  constrain(forwardButton) {
    width = Dimension.value(36.dp)
    height = Dimension.value(36.dp)
    start.linkTo(playButton.end)
    top.linkTo(episodeImage.top)
    bottom.linkTo(episodeImage.bottom)
  }

  constrain(playlistIcon) {
    width = Dimension.value(36.dp)
    height = Dimension.value(36.dp)
    top.linkTo(episodeImage.top)
    bottom.linkTo(episodeImage.bottom)
  }

  constrain(playbackProgress) {
    top.linkTo(episodeImage.bottom, margin = 2.dp)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
  }

  constrain(playbackSpeed) {
    visibility = Visibility.Invisible
    start.linkTo(parent.start)
    end.linkTo(parent.end)
  }
}

@Composable
private fun endConstraintSet(
  episodeImageId: String,
  playlistIconId: String,
  playButtonId: String,
  rewindButtonId: String,
  forwardButtonId: String,
  episodeTitleId: String,
  playbackProgressId: String,
  playbackSpeedId: String
) = ConstraintSet {
  val episodeImage = createRefFor(episodeImageId)
  val playlistIcon = createRefFor(playlistIconId)
  val playButton = createRefFor(playButtonId)
  val rewindButton = createRefFor(rewindButtonId)
  val forwardButton = createRefFor(forwardButtonId)
  val episodeTitle = createRefFor(episodeTitleId)
  val playbackProgress = createRefFor(playbackProgressId)
  val playbackSpeed = createRefFor(playbackSpeedId)

  createHorizontalChain(
    rewindButton,
    playButton.withChainParams(startMargin = 16.dp),
    forwardButton.withChainParams(startMargin = 16.dp),
    chainStyle = ChainStyle.Spread
  )

  constrain(playlistIcon) {
    width = Dimension.value(36.dp)
    height = Dimension.value(36.dp)
    top.linkTo(parent.top, margin = 8.dp)
    end.linkTo(parent.end, margin = 8.dp)
  }

  constrain(episodeImage) {
    width = Dimension.value(350.dp)
    height = Dimension.value(350.dp)
    top.linkTo(playlistIcon.bottom, margin = 16.dp)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
  }

  constrain(episodeTitle) {
    top.linkTo(episodeImage.bottom, margin = 48.dp)
    start.linkTo(parent.start, margin = 24.dp)
    end.linkTo(parent.end, margin = 24.dp)
  }

  constrain(playButton) {
    width = Dimension.value(96.dp)
    height = Dimension.value(96.dp)
    top.linkTo(episodeTitle.bottom, margin = 24.dp)
  }

  constrain(rewindButton) {
    width = Dimension.value(64.dp)
    height = Dimension.value(64.dp)
    start.linkTo(parent.end)
    top.linkTo(playButton.top)
    bottom.linkTo(playButton.bottom)
  }

  constrain(forwardButton) {
    width = Dimension.value(64.dp)
    height = Dimension.value(64.dp)
    top.linkTo(playButton.top)
    bottom.linkTo(playButton.bottom)
    end.linkTo(parent.end)
  }

  constrain(playbackProgress) {
    width = Dimension.fillToConstraints
    top.linkTo(playButton.bottom, margin = 16.dp)
    start.linkTo(parent.start, margin = 24.dp)
    end.linkTo(parent.end, margin = 24.dp)
  }

  constrain(playbackSpeed) {
    top.linkTo(playbackProgress.bottom)
    start.linkTo(parent.start, margin = 24.dp)
    bottom.linkTo(parent.bottom, margin = 48.dp)
  }
}

@Preview(heightDp = 56, showBackground = true)
@Composable
fun MiniNowPlayingMotionViewPreview() {
  CastawayTheme(MATERIAL, true) {
    NowPlayingMotionView(
      expandedPercentage = { 0f },
      episode = {
        NowPlayingEpisode(
          id = "uu1d",
          title = "Awesome Episode 1",
          subTitle = "How to be just awesome!",
          description = "In this episode...",
          audioUrl = "episode.url",
          imageUrl = "image.url",
          author = "Awesom-O",
          playbackPosition = NowPlayingPosition(position = 1800000L, duration = 2160000L),
          episode = 1,
          podcastUrl = "pod.url"
        )
      },
      playing = { true },
      playbackSpeed = { 1.5f }
    ) {}
  }
}

@Preview
@Composable
fun NowPlayingMotionViewPreview() {
  CastawayTheme(MATERIAL, true) {
    NowPlayingMotionView(
      expandedPercentage = { 1f },
      episode = {
        NowPlayingEpisode(
          id = "uu1d",
          title = "Awesome Episode 1",
          subTitle = "How to be just awesome!",
          description = "In this episode...",
          audioUrl = "episode.url",
          imageUrl = "image.url",
          author = "Awesom-O",
          playbackPosition = NowPlayingPosition(position = 1800000L, duration = 2160000L),
          episode = 1,
          podcastUrl = "pod.url"
        )
      },
      playing = { true },
      playbackSpeed = { 1.5f }
    ) {}
  }
}
