package io.github.lazyengineer.castaway.androidApp.view.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.R
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import io.github.lazyengineer.castaway.domain.resource.ThemeType.MATERIAL

@Composable
fun OnBoardingScreen(
  modifier: Modifier = Modifier,
  darkTheme: Boolean = CastawayTheme.colors.isDark,
  switchTheme: (Boolean) -> Unit,
  finished: (Boolean) -> Unit
) {

  val switchState = rememberSaveable { mutableStateOf(darkTheme) }

  Surface(modifier = modifier.fillMaxSize()) {
	Column(
	  modifier = Modifier
		.fillMaxSize()
		.background(CastawayTheme.colors.background)
		.padding(16.dp),
	  verticalArrangement = Arrangement.SpaceAround,
	  horizontalAlignment = Alignment.CenterHorizontally
	) {

	  Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	  ) {
		Text(
		  text = stringResource(id = R.string.onboarding_choose),
		  color = CastawayTheme.colors.onBackground,
		  style = MaterialTheme.typography.h5,
		  modifier = Modifier
			.padding(16.dp)
			.semantics {
			  contentDescription = "Choose theme title"
			}
		)

		Text(
		  text = when (switchState.value) {
			true -> stringResource(id = R.string.onboarding_darkmode_desc)
			false -> stringResource(id = R.string.onboarding_lightmode_desc)
		  },
		  color = CastawayTheme.colors.onBackground,
		  style = MaterialTheme.typography.h6,
		  modifier = Modifier
			.padding(16.dp)
			.semantics {
			  contentDescription = "Choose theme description"
			}
		)
	  }

	  Row {
		Text("☀️", modifier = Modifier.padding(end = 16.dp))

		Switch(
		  checked = switchState.value,
		  colors = SwitchDefaults.colors(
			checkedThumbColor = CastawayTheme.colors.primary,
			checkedTrackColor = CastawayTheme.colors.primaryVariant,
			uncheckedThumbColor = CastawayTheme.colors.primary,
			uncheckedTrackColor = CastawayTheme.colors.primaryVariant
		  ),
		  onCheckedChange = { checked ->
			switchTheme(checked)
			switchState.value = checked
		  })

		Text("🌙", modifier = Modifier.padding(start = 16.dp))
	  }

	  GradientTextButton(
		modifier
		  .width(240.dp)
		  .height(48.dp),
		text = stringResource(id = R.string.onboarding_continue).uppercase(),
		gradient = Brush.linearGradient(CastawayTheme.colors.gradient),
		shape = RoundedCornerShape(16.dp),
	  ) {
		finished(true)
	  }
	}
  }
}

@Preview
@Composable
fun OnBoardingScreen_Dark_Preview() {
  val darkTheme = true

  CastawayTheme(MATERIAL, darkTheme) {
	OnBoardingScreen(darkTheme = darkTheme, switchTheme = {}) {}
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
