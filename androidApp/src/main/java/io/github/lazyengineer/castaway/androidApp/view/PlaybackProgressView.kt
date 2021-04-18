package io.github.lazyengineer.castaway.androidApp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun PlaybackProgressView(modifier: Modifier) {
  Box(
	modifier = modifier
  ) {
	
	Box(modifier = modifier.height(4.dp).clip(RoundedCornerShape(50)).background(MaterialTheme.colors.primary).zIndex(1f))
	Box(modifier = modifier.height(4.dp).padding(end = 48.dp).clip(RoundedCornerShape(50)).background(Color.Black).zIndex(1f))
  }
}