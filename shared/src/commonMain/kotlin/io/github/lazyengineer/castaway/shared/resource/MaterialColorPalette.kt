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
import io.github.lazyengineer.castaway.shared.resource.ThemeType.MATERIAL

class MaterialColorPalette(val darkMode: Boolean = true) : CastawayColorPalette {

  override val primary: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(orangeGradientStart)
	  false -> ThemedColor.Light(blueGradientStart)
	}

  override val primaryVariant: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(orangeGradientMiddle)
	  false -> ThemedColor.Light(blueGradientMiddle)
	}

  override val secondary: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(orangeGradientEnd)
	  false -> ThemedColor.Light(blueGradientEnd)
	}

  override val secondaryVariant: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(orangeGradientEnd)
	  false -> ThemedColor.Light(blueGradientEnd)
	}

  override val background: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(darkThemeBackground)
	  false -> ThemedColor.Light(lightThemeBackground)
	}

  override val surface: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(darkThemeBackground)
	  false -> ThemedColor.Light(lightThemeBackground)
	}

  override val error: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(red)
	  false -> ThemedColor.Light(red)
	}

  override val onPrimary: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(darkThemeTextColor)
	  false -> ThemedColor.Light(lightThemeTextColor)
	}

  override val onSecondary: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(darkThemeTextColor)
	  false -> ThemedColor.Light(lightThemeTextColor)
	}

  override val onBackground: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(darkThemeTextColor)
	  false -> ThemedColor.Light(lightThemeTextColor)
	}

  override val onSurface: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(white)
	  false -> ThemedColor.Light(white)
	}

  override val onError: ThemedColor
	get() = when (darkMode) {
	  true -> ThemedColor.Dark(white)
	  false -> ThemedColor.Light(white)
	}

  override val isDark: Boolean
	get() = darkMode

  override val themeType: ThemeType
	get() = MATERIAL
}
