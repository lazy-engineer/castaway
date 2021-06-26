package io.github.lazyengineer.castaway.androidApp.view.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier, darkTheme: Boolean = isSystemInDarkTheme(), switchTheme: (Boolean) -> Unit, finished: (Boolean) -> Unit) {

  val switchState = rememberSaveable { mutableStateOf(darkTheme) }

  Surface(modifier = modifier.fillMaxSize()) {
	Column(
	  modifier = Modifier.fillMaxSize(),
	  verticalArrangement = Arrangement.SpaceAround,
	  horizontalAlignment = Alignment.CenterHorizontally
	) {

	  Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	  ) {
		Text("Choose Your Destiny", style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))

		Text(
		  text = when (switchState.value) {
			true -> "\"The night is darkest just before the dawn. And I promise you, the dawn is coming.\"\n - Harvey Dent"
			false -> "\"- Hamid: What's that?\n - Rambo: It's blue light.\n - Hamid: What does it do?\n - Rambo: It turns blue.\""
		  }, style = MaterialTheme.typography.h6, modifier = Modifier.padding(16.dp)
		)
	  }

	  Row {
		Text("☀️", modifier = Modifier.padding(end = 16.dp))

		Switch(checked = switchState.value, onCheckedChange = { checked ->
		  switchTheme(checked)
		  switchState.value = checked
		})

		Text("🌙", modifier = Modifier.padding(start = 16.dp))
	  }

	  Button(
		modifier = Modifier
		  .padding(48.dp)
		  .clip(CircleShape),
		onClick = {
		  finished(true)
		}) {
		Text("👌")
	  }
	}
  }
}