package io.github.lazyengineer.castaway.androidApp.view.podcast

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState

@Composable
fun PodcastHeaderView(modifier: Modifier, title: String, imageUrl: String) {
  Column(
	modifier = modifier,
	horizontalAlignment = Alignment.CenterHorizontally,
  ) {
	Text(
	  title,
	  modifier = Modifier.padding(top = 16.dp),
	  style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
	)

	val painter = rememberCoilPainter(imageUrl)

	Image(
	  painter = painter,
	  modifier = Modifier.padding(top = 48.dp, bottom = 48.dp).width(150.dp).clip(RoundedCornerShape(25f)),
	  contentDescription = "Podcast header image",
	)

	when (painter.loadState) {
	  is ImageLoadState.Loading -> {
		CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
	  }
	  is ImageLoadState.Error -> {
		Icon(Filled.Mic, "Podcast header icon", modifier = Modifier.size(150.dp), tint = Color.Gray)
	  }
	}
  }
}