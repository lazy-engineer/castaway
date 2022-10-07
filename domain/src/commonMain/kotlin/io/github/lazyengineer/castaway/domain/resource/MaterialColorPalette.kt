package io.github.lazyengineer.castaway.domain.resource

import io.github.lazyengineer.castaway.domain.resource.Colors.azurGradientEnd
import io.github.lazyengineer.castaway.domain.resource.Colors.azurGradientMiddle
import io.github.lazyengineer.castaway.domain.resource.Colors.azurGradientStart
import io.github.lazyengineer.castaway.domain.resource.Colors.blueGradientEnd
import io.github.lazyengineer.castaway.domain.resource.Colors.blueGradientMiddle
import io.github.lazyengineer.castaway.domain.resource.Colors.blueGradientStart
import io.github.lazyengineer.castaway.domain.resource.Colors.darkThemeBackground
import io.github.lazyengineer.castaway.domain.resource.Colors.darkThemeDarkShadow
import io.github.lazyengineer.castaway.domain.resource.Colors.darkThemeLightShadow
import io.github.lazyengineer.castaway.domain.resource.Colors.darkThemeTextColor
import io.github.lazyengineer.castaway.domain.resource.Colors.lightThemeBackground
import io.github.lazyengineer.castaway.domain.resource.Colors.lightThemeTextColor
import io.github.lazyengineer.castaway.domain.resource.Colors.orangeGradientEnd
import io.github.lazyengineer.castaway.domain.resource.Colors.orangeGradientMiddle
import io.github.lazyengineer.castaway.domain.resource.Colors.orangeGradientStart
import io.github.lazyengineer.castaway.domain.resource.Colors.red
import io.github.lazyengineer.castaway.domain.resource.Colors.white
import io.github.lazyengineer.castaway.domain.resource.ThemeType.MATERIAL
import io.github.lazyengineer.castaway.domain.resource.ThemedColor.Dark
import io.github.lazyengineer.castaway.domain.resource.ThemedColor.Light

class MaterialColorPalette(private val darkMode: Boolean = true) : CastawayColorPalette {

  override val primary: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(orangeGradientStart)
	  false -> Light(blueGradientStart)
	}

  override val primaryVariant: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(orangeGradientMiddle)
	  false -> Light(blueGradientMiddle)
	}

  override val secondary: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(orangeGradientEnd)
	  false -> Light(blueGradientEnd)
	}

  override val secondaryVariant: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(orangeGradientEnd)
	  false -> Light(blueGradientEnd)
	}

  override val background: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(darkThemeBackground)
	  false -> Light(lightThemeBackground)
	}

  override val surface: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(darkThemeBackground)
	  false -> Light(lightThemeBackground)
	}

  override val error: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(red)
	  false -> Light(red)
	}

  override val onPrimary: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(darkThemeTextColor)
	  false -> Light(lightThemeTextColor)
	}

  override val onSecondary: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(darkThemeTextColor)
	  false -> Light(lightThemeTextColor)
	}

  override val onBackground: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(darkThemeTextColor)
	  false -> Light(lightThemeTextColor)
	}

  override val onSurface: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(white)
	  false -> Light(white)
	}

  override val onError: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(white)
	  false -> Light(white)
	}

  override val shadow: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(darkThemeDarkShadow)
	  false -> Light(darkThemeDarkShadow)
	}

  override val reflection: ThemedColor
	get() = when (darkMode) {
	  true -> Dark(darkThemeLightShadow)
	  false -> Light(darkThemeLightShadow)
	}

  override val gradient: List<ThemedColor>
	get() = when (darkMode) {
	  true -> listOf(Dark(orangeGradientEnd), Dark(orangeGradientStart), Dark(orangeGradientMiddle))
	  false -> listOf(Light(azurGradientStart), Light(azurGradientMiddle), Light(azurGradientEnd))
	}

  override val isDark: Boolean
	get() = darkMode

  override val themeType: ThemeType
	get() = MATERIAL
}
