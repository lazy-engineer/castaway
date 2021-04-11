package io.github.lazyengineer.castaway.androidApp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EpisodeRowView(title: String) {
  Column {
	Row(
	  modifier = Modifier.height(70.dp).fillMaxSize().padding(8.dp),
	  horizontalArrangement = Arrangement.SpaceBetween,
	  verticalAlignment = Alignment.CenterVertically,
	) {
	  Text(title, modifier = Modifier.weight(5f))

	  Icon(Filled.PlayArrow, "play", modifier = Modifier.padding(8.dp).weight(1f))
	}
	Divider()
  }
}