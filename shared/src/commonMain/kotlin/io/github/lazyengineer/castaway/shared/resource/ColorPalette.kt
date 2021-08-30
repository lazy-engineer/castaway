package io.github.lazyengineer.castaway.shared.resource

import io.github.lazyengineer.castaway.shared.resource.Colors.blueGradientEnd
import io.github.lazyengineer.castaway.shared.resource.Colors.blueGradientMiddle
import io.github.lazyengineer.castaway.shared.resource.Colors.blueGradientStart
import io.github.lazyengineer.castaway.shared.resource.Colors.darkThemeBackground
import io.github.lazyengineer.castaway.shared.resource.Colors.darkThemeTextColor
import io.github.lazyengineer.castaway.shared.resource.Colors.lightThemeBackground
import io.github.lazyengineer.castaway.shared.resource.Colors.lightThemeTextColor
import io.github.lazyengineer.castaway.shared.resource.Colors.orangeGradientEnd
import io.github.lazyengineer.castaway.shared.resource.Colors.orangeGradientMiddle
import io.github.lazyengineer.castaway.shared.resource.Colors.orangeGradientStart
import io.github.lazyengineer.castaway.shared.resource.Colors.red
import io.github.lazyengineer.castaway.shared.resource.Colors.white

class ColorPalette(val darkMode: Boolean = true) {

  val primary = when (darkMode) {
	true -> ThemedColor.Dark(orangeGradientStart)
	false -> ThemedColor.Light(blueGradientStart)
  }

  val primaryVariant = when (darkMode) {
	true -> ThemedColor.Dark(orangeGradientMiddle)
	false -> ThemedColor.Light(blueGradientMiddle)
  }

  val secondary = when (darkMode) {
	true -> ThemedColor.Dark(orangeGradientEnd)
	false -> ThemedColor.Light(blueGradientEnd)
  }

  val secondaryVariant = when (darkMode) {
	true -> ThemedColor.Dark(orangeGradientEnd)
	false -> ThemedColor.Light(blueGradientEnd)
  }

  val background = when (darkMode) {
	true -> ThemedColor.Dark(darkThemeBackground)
	false -> ThemedColor.Light(lightThemeBackground)
  }

  val surface = when (darkMode) {
	true -> ThemedColor.Dark(darkThemeBackground)
	false -> ThemedColor.Light(lightThemeBackground)
  }

  val error = when (darkMode) {
	true -> ThemedColor.Dark(red)
	false -> ThemedColor.Light(red)
  }

  val onPrimary = when (darkMode) {
	true -> ThemedColor.Dark(darkThemeTextColor)
	false -> ThemedColor.Light(lightThemeTextColor)
  }

  val onSecondary = when (darkMode) {
	true -> ThemedColor.Dark(darkThemeTextColor)
	false -> ThemedColor.Light(lightThemeTextColor)
  }

  val onBackground = when (darkMode) {
	true -> ThemedColor.Dark(darkThemeTextColor)
	false -> ThemedColor.Light(lightThemeTextColor)
  }

  val onSurface = when (darkMode) {
	true -> ThemedColor.Dark(white)
	false -> ThemedColor.Light(white)
  }

  val onError = when (darkMode) {
	true -> ThemedColor.Dark(white)
	false -> ThemedColor.Light(white)
  }
}
