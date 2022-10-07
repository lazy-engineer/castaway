package io.github.lazyengineer.castaway.domain.resource

interface CastawayColorPalette {

  val primary: ThemedColor
  val primaryVariant: ThemedColor
  val secondary: ThemedColor
  val secondaryVariant: ThemedColor
  val background: ThemedColor
  val surface: ThemedColor
  val error: ThemedColor
  val onPrimary: ThemedColor
  val onSecondary: ThemedColor
  val onBackground: ThemedColor
  val onSurface: ThemedColor
  val onError: ThemedColor
  val shadow: ThemedColor
  val reflection: ThemedColor
  val gradient: List<ThemedColor>
  val isDark: Boolean
  val themeType: ThemeType
}
