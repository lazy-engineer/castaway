package io.github.lazyengineer.castaway.androidApp.view.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun OnBoardingScreen(isDarkTheme: Boolean = isSystemInDarkTheme()) {

  val switchState = remember { mutableStateOf(isDarkTheme) }
  val themeDescription = if (switchState.value) "\"The night is darkest just before the dawn. And I promise you, the dawn is coming.\" \n - Harvey Dent" else "\"- Hamid: What's that? \n - Rambo: It's blue light. \n - Hamid: What does it do? \n - Rambo: It turns blue.\""

  Column(
	modifier = Modifier.fillMaxWidth().fillMaxHeight(),
	verticalArrangement = Arrangement.Center,
	horizontalAlignment = Alignment.CenterHorizontally
  ) {
	Text("Choose Your Destiny", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))

	Text(themeDescription, style = MaterialTheme.typography.h6, modifier = Modifier.padding(16.dp))

	Row {
	  Text("☀️", modifier = Modifier.padding(end = 16.dp))

	  Switch(checked = switchState.value, onCheckedChange = { checked ->
		switchState.value = checked
	  })

	  Text("🌙", modifier = Modifier.padding(start = 16.dp))
	}

	Button(
	  modifier = Modifier
		.padding(top = 48.dp)
		.clip(CircleShape),
	  onClick = {

	  }) {
	  Text("👌")
	}
  }
}