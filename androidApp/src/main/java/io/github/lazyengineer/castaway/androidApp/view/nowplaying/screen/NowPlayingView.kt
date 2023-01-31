//package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.CircularProgressIndicator
//import androidx.compose.material.Icon
//import androidx.compose.material.IconButton
//import androidx.compose.material.Text
//import androidx.compose.material.icons.Icons.Filled
//import androidx.compose.material.icons.filled.Forward30
//import androidx.compose.material.icons.filled.Mic
//import androidx.compose.material.icons.filled.PauseCircleFilled
//import androidx.compose.material.icons.filled.PlayCircleFilled
//import androidx.compose.material.icons.filled.Replay30
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import coil.compose.SubcomposeAsyncImage
//import coil.request.ImageRequest.Builder
//import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEpisode
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditPlaybackPosition
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditPlaybackSpeed
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.EditingPlayback
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.FastForward
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.PlayPause
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.Rewind
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingEvent.SeekTo
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.NowPlayingPosition
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.util.millisToTxt
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.util.playbackProgress
//import io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.util.progressToPosition
//import io.github.lazyengineer.castaway.androidApp.view.shared.PlaybackSliderView
//import io.github.lazyengineer.castaway.androidApp.view.shared.PlaybackTrackView
//import io.github.lazyengineer.castaway.androidApp.view.style.shadow
//
//@Composable
//internal fun NowPlayingView(
//  episode: NowPlayingEpisode,
//  playing: Boolean,
//  playbackSpeed: Float,
//  modifier: Modifier = Modifier,
//  event: (NowPlayingEvent) -> Unit
//) {
//  Column(
//	modifier = modifier.background(CastawayTheme.colors.background),
//	horizontalAlignment = Alignment.CenterHorizontally
//  ) {
//	EpisodeImage(
//	  imageUrl = episode.imageUrl,
//	  modifier = Modifier.padding(top = 64.dp, bottom = 64.dp)
//	)
//
//	EpisodeTitle(
//	  episodeTitle = episode.title,
//	  modifier = Modifier.padding(top = 16.dp, bottom = 48.dp)
//	)
//
//	PlaybackControls(
//	  episodeId = episode.id,
//	  playing = playing,
//	  event = event,
//	)
//
//	PlaybackProgress(
//	  playbackPosition = episode.playbackPosition,
//	  event = event,
//	  modifier = Modifier.padding(top = 64.dp)
//	)
//
//	PlaybackSpeed(
//	  playbackSpeed = playbackSpeed,
//	  modifier = Modifier
//		.align(alignment = Alignment.Start)
//		.padding(start = 16.dp),
//	  event = event
//	)
//  }
//}
//
//@Composable
//private fun PlaybackProgress(
//  playbackPosition: NowPlayingPosition,
//  modifier: Modifier = Modifier,
//  event: (NowPlayingEvent) -> Unit
//) {
//  Column(modifier = modifier.fillMaxWidth()) {
//	Row(
//	  modifier = Modifier
//		.fillMaxWidth()
//		.padding(start = 16.dp, end = 16.dp), horizontalArrangement = Arrangement.SpaceBetween
//	) {
//	  Text(playbackPosition.safePosition.millisToTxt(), color = CastawayTheme.colors.onBackground)
//	  Text(playbackPosition.duration.millisToTxt(), color = CastawayTheme.colors.onBackground)
//	}
//
//	PlaybackTrackView(
//	  modifier = Modifier
//		.fillMaxWidth()
//		.padding(start = 16.dp, end = 16.dp),
//	  progress = playbackProgress(
//		playbackPosition.safePosition,
//		playbackPosition.duration
//	  ),
//	  onValueChange = {
//		event(
//		  EditPlaybackPosition(
//			it.progressToPosition(
//			  playbackPosition.duration
//			)
//		  )
//		)
//	  },
//	  onValueChangeStarted = {
//		event(EditingPlayback(true))
//	  },
//	  onValueChangeFinished = {
//		event(EditingPlayback(false))
//		event(SeekTo(playbackPosition.safePosition))
//	  })
//  }
//}
//
//@Composable
//private fun EpisodeTitle(
//  episodeTitle: String,
//  modifier: Modifier = Modifier
//) {
//  Text(
//	text = episodeTitle,
//	color = CastawayTheme.colors.onBackground,
//	modifier = modifier
//  )
//}
//
//@Composable
//private fun PlaybackSpeed(
//  playbackSpeed: Float,
//  modifier: Modifier = Modifier,
//  event: (NowPlayingEvent) -> Unit
//) {
//  Text(
//	text = "${playbackSpeed}x",
//	color = CastawayTheme.colors.onBackground,
//	modifier = modifier
//	  .clickable {
//		event(EditPlaybackSpeed(2f))
//	  })
//}
//
//@Composable
//private fun PlaybackControls(
//  episodeId: String,
//  playing: Boolean,
//  modifier: Modifier = Modifier,
//  event: (NowPlayingEvent) -> Unit,
//) {
//  Row(
//	modifier = modifier.fillMaxWidth(),
//	verticalAlignment = Alignment.CenterVertically,
//	horizontalArrangement = Arrangement.Center
//  ) {
//	IconButton(
//	  onClick = {
//		event(Rewind)
//	  }) {
//	  Icon(
//		imageVector = Filled.Replay30,
//		contentDescription = "replay 30 second",
//		tint = CastawayTheme.colors.onBackground,
//		modifier = Modifier.size(48.dp)
//	  )
//	}
//
//	IconButton(
//	  modifier = Modifier
//		.padding(start = 48.dp, end = 48.dp)
//		.size(64.dp),
//	  onClick = {
//		event(PlayPause(episodeId))
//	  }) {
//
//	  val playPauseImage = when (playing) {
//		true -> Filled.PauseCircleFilled
//		else -> Filled.PlayCircleFilled
//	  }
//
//	  Icon(
//		imageVector = playPauseImage,
//		contentDescription = "play/pause",
//		tint = CastawayTheme.colors.onBackground,
//		modifier = Modifier.size(64.dp)
//	  )
//	}
//	IconButton(onClick = {
//	  event(FastForward)
//	}) {
//	  Icon(
//		imageVector = Filled.Forward30,
//		contentDescription = "fast forward 30 second",
//		tint = CastawayTheme.colors.onBackground,
//		modifier = Modifier.size(48.dp)
//	  )
//	}
//  }
//}
//
//@Composable
//private fun EpisodeImage(
//  imageUrl: String?,
//  modifier: Modifier = Modifier
//) {
//  Box(modifier = modifier) {
//	Box(
//	  modifier = Modifier
//		.size(300.dp)
//		.clip(RoundedCornerShape(25f))
//	)
//
//	SubcomposeAsyncImage(
//	  model = Builder(LocalContext.current)
//		.data(imageUrl)
//		.crossfade(true)
//		.build(),
//	  loading = {
//		CircularProgressIndicator(color = CastawayTheme.colors.primary)
//	  },
//	  error = {
//		Icon(
//		  imageVector = Filled.Mic,
//		  contentDescription = "Podcast header icon",
//		  modifier = Modifier.size(150.dp),
//		  tint = Color.Gray
//		)
//	  },
//	  contentDescription = "Podcast header image",
//	  contentScale = ContentScale.Crop,
//	  modifier = Modifier
//		.size(300.dp)
//		.padding(20.dp)
//		.shadow(CastawayTheme.colors.reflection, shadowRadius = 12.dp, offsetX = (-8).dp, offsetY = (-8).dp)
//		.shadow(CastawayTheme.colors.shadow, shadowRadius = 12.dp, offsetX = 8.dp, offsetY = 8.dp)
//		.clip(RoundedCornerShape(25f))
//	)
//  }
//}
