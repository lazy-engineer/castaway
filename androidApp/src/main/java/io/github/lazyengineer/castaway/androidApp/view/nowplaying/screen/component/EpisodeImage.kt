package io.github.lazyengineer.castaway.androidApp.view.nowplaying.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest.Builder
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme

@Composable
internal fun EpisodeImage(
  imageUrl: String?,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier) {
    SubcomposeAsyncImage(
      model = Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build(),
      loading = {
        CircularProgressIndicator(color = CastawayTheme.colors.primary)
      },
      error = {
        Icon(
          imageVector = Filled.Mic,
          contentDescription = "Podcast header icon",
          modifier = Modifier.fillMaxSize(),
          tint = Color.Gray
        )
      },
      contentDescription = "Podcast header image",
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize()
    )
  }
}
