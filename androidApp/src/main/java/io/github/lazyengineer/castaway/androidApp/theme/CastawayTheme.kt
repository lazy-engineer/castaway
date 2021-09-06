package io.github.lazyengineer.castaway.androidApp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import io.github.lazyengineer.castaway.androidApp.ext.toColor
import io.github.lazyengineer.castaway.shared.resource.CastawayColorPalette
import io.github.lazyengineer.castaway.shared.resource.ThemeType
import io.github.lazyengineer.castaway.shared.resource.ThemeType.MATERIAL
import io.github.lazyengineer.castaway.shared.resource.ThemeType.NEUMORPHISM

@Composable
fun CastawayTheme(
  themeType: ThemeType,
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable() () -> Unit
) {
  when (themeType) {
	MATERIAL -> ThemeMaterial(darkTheme, content)
	NEUMORPHISM -> ThemeNeumorphism(darkTheme, content)
  }
}

object CastawayTheme {

  val colors: CastawayColors
	@Composable
	get() = LocalCastawayColors.current
}

private val LocalCastawayColors = staticCompositionLocalOf<CastawayColors> {
  error("No CastawayColors provided")
}

@Composable
fun ProvideCastawayColors(
  colorsPalette: CastawayColorPalette,
  content: @Composable () -> Unit
) {
  val colors = colorsPalette.toColors()

  val colorPalette = remember {
	// Explicitly creating a new object here so we don't mutate the initial [colors]
	// provided, and overwrite the values set in it.
	colors.copy()
  }

  colorPalette.update(colors)
  CompositionLocalProvider(LocalCastawayColors provides colorPalette, content = content)
}

private fun CastawayColorPalette.toColors(): CastawayColors {
  return CastawayColors(
	primary = primary.toColor(),
	primaryVariant = primaryVariant.toColor(),
	secondary = secondary.toColor(),
	secondaryVariant = secondaryVariant.toColor(),
	background = background.toColor(),
	surface = surface.toColor(),
	error = error.toColor(),
	onPrimary = onPrimary.toColor(),
	onSecondary = onSecondary.toColor(),
	onBackground = onBackground.toColor(),
	onSurface = onSurface.toColor(),
	onError = onError.toColor(),
	gradient = gradient.map { it.toColor() },
	isDark = isDark,
	themeType = themeType,
  )
}

class CastawayColors(
  primary: Color,
  primaryVariant: Color,
  secondary: Color,
  secondaryVariant: Color,
  background: Color,
  surface: Color,
  error: Color,
  onPrimary: Color,
  onSecondary: Color,
  onBackground: Color,
  onSurface: Color,
  onError: Color,
  gradient: List<Color>,
  isDark: Boolean,
  themeType: ThemeType,
) {

  var primary by mutableStateOf(primary)
	private set
  var primaryVariant by mutableStateOf(primaryVariant)
	private set
  var secondary by mutableStateOf(secondary)
	private set
  var secondaryVariant by mutableStateOf(secondaryVariant)
	private set
  var background by mutableStateOf(background)
	private set
  var surface by mutableStateOf(surface)
	private set
  var error by mutableStateOf(error)
	private set
  var onPrimary by mutableStateOf(onPrimary)
	private set
  var onSecondary by mutableStateOf(onSecondary)
	private set
  var onBackground by mutableStateOf(onBackground)
	private set
  var onSurface by mutableStateOf(onSurface)
	private set
  var onError by mutableStateOf(onError)
	private set
  var gradient by mutableStateOf(gradient)
	private set
  var isDark by mutableStateOf(isDark)
	private set
  var themeType by mutableStateOf(themeType)
	private set

  fun update(other: CastawayColors) {
	primary = other.primary
	primaryVariant = other.primaryVariant
	secondary = other.secondary
	secondaryVariant = other.secondaryVariant
	background = other.background
	surface = other.surface
	error = other.error
	onPrimary = other.onPrimary
	onSecondary = other.onSecondary
	onBackground = other.onBackground
	onSurface = other.onSurface
	onError = other.onError
	gradient = other.gradient
	isDark = other.isDark
	themeType = other.themeType
  }

  fun copy(): CastawayColors = CastawayColors(
	primary = primary,
	primaryVariant = primaryVariant,
	secondary = secondary,
	secondaryVariant = secondaryVariant,
	background = background,
	surface = surface,
	error = error,
	onPrimary = onPrimary,
	onSecondary = onSecondary,
	onBackground = onBackground,
	onSurface = onSurface,
	onError = onError,
	gradient = gradient,
	isDark = isDark,
	themeType = themeType,
  )
}

/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colors] in preference to [CastawayTheme.colors].
 */
fun debugColors(
  darkTheme: Boolean,
  debugColor: Color = if (darkTheme) Color.Cyan else Color.Magenta
) = Colors(
  primary = debugColor,
  primaryVariant = debugColor,
  secondary = debugColor,
  secondaryVariant = debugColor,
  background = debugColor,
  surface = debugColor,
  error = debugColor,
  onPrimary = debugColor,
  onSecondary = debugColor,
  onBackground = debugColor,
  onSurface = debugColor,
  onError = debugColor,
  isLight = !darkTheme
)
