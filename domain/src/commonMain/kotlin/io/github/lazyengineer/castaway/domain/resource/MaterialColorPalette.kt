package io.github.lazyengineer.castaway.domain.resource

import io.github.lazyengineer.castaway.domain.resource.Colors.AzurGradientEnd
import io.github.lazyengineer.castaway.domain.resource.Colors.AzurGradientMiddle
import io.github.lazyengineer.castaway.domain.resource.Colors.AzurGradientStart
import io.github.lazyengineer.castaway.domain.resource.Colors.Material
import io.github.lazyengineer.castaway.domain.resource.Colors.OrangeGradientEnd
import io.github.lazyengineer.castaway.domain.resource.Colors.OrangeGradientMiddle
import io.github.lazyengineer.castaway.domain.resource.Colors.OrangeGradientStart
import io.github.lazyengineer.castaway.domain.resource.Colors.Red
import io.github.lazyengineer.castaway.domain.resource.Colors.White
import io.github.lazyengineer.castaway.domain.resource.ThemeType.MATERIAL
import io.github.lazyengineer.castaway.domain.resource.ThemedColor.Dark
import io.github.lazyengineer.castaway.domain.resource.ThemedColor.Light

class MaterialColorPalette(private val darkMode: Boolean = true) : CastawayColorPalette {

  override val primary: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkPrimary)
      false -> Light(Material.LightPrimary)
    }

  override val primaryVariant: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkPrimaryVariant)
      false -> Light(Material.LightPrimaryVariant)
    }

  override val secondary: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkSecondary)
      false -> Light(Material.LightSecondary)
    }

  override val secondaryVariant: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkSecondaryVariant)
      false -> Light(Material.LightSecondaryVariant)
    }

  override val background: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkThemeBackground)
      false -> Light(Material.LightThemeBackground)
    }

  override val surface: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkThemeBackground)
      false -> Light(Material.LightThemeBackground)
    }

  override val error: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Red)
      false -> Light(Red)
    }

  override val onPrimary: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkThemeTextColor)
      false -> Light(Material.LightThemeTextColor)
    }

  override val onSecondary: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkThemeTextColor)
      false -> Light(Material.LightThemeTextColor)
    }

  override val onBackground: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkThemeTextColor)
      false -> Light(Material.LightThemeTextColor)
    }

  override val onSurface: ThemedColor
    get() = when (darkMode) {
      true -> Dark(White)
      false -> Light(White)
    }

  override val onError: ThemedColor
    get() = when (darkMode) {
      true -> Dark(White)
      false -> Light(White)
    }

  override val shadow: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkThemeDarkShadow)
      false -> Light(Material.DarkThemeDarkShadow)
    }

  override val reflection: ThemedColor
    get() = when (darkMode) {
      true -> Dark(Material.DarkThemeLightShadow)
      false -> Light(Material.DarkThemeLightShadow)
    }

  override val gradient: List<ThemedColor>
    get() = when (darkMode) {
      true -> listOf(Dark(OrangeGradientEnd), Dark(OrangeGradientStart), Dark(OrangeGradientMiddle))
      false -> listOf(Light(AzurGradientStart), Light(AzurGradientMiddle), Light(AzurGradientEnd))
    }

  override val isDark: Boolean
    get() = darkMode

  override val themeType: ThemeType
    get() = MATERIAL
}
