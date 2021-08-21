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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.R
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.androidApp.theme.ThemeType.MATERIAL

@Composable
fun OnBoardingScreen(
  modifier: Modifier = Modifier,
  darkTheme: Boolean = isSystemInDarkTheme(),
  switchTheme: (Boolean) -> Unit,
  finished: (Boolean) -> Unit
) {

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
		Text(stringResource(id = R.string.onboarding_choose), style = MaterialTheme.typography.h5, modifier = Modifier.padding(16.dp))

		Text(
		  text = when (switchState.value) {
			true -> stringResource(id = R.string.onboarding_darkmode_desc)
			false -> stringResource(id = R.string.onboarding_lightmode_desc)
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
		Text(stringResource(id = R.string.onboarding_emoji))
	  }
	}
  }
}

@Preview
@Composable
fun OnBoardingScreen_Dark_Preview() {
  val darkTheme = true

  CastawayTheme(MATERIAL, darkTheme) {
	OnBoardingScreen(darkTheme = darkTheme,switchTheme = {}) {}
  }
}

@Preview
@Composable
fun OnBoardingScreen_Light_Preview() {
  val darkTheme = false

  CastawayTheme(MATERIAL, darkTheme) {
	OnBoardingScreen(darkTheme = darkTheme, switchTheme = {}) {}
  }
}
