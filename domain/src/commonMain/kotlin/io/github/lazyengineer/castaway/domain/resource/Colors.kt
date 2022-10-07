package io.github.lazyengineer.castaway.domain.resource

object Colors {

  val gray = CommonColor(red = 128, green = 128, blue = 128)
  val white = CommonColor(red = 255, green = 255, blue = 255)
  val black = CommonColor(red = 0, green = 0, blue = 0)
  val darkGray = CommonColor(red = 97, green = 97, blue = 97)
  val red = CommonColor(red = 255, green = 0, blue = 0)

  val lightThemeBackground = CommonColor(red = 225, green = 234, blue = 252)
  val darkThemeBackground = CommonColor(red = 41, green = 45, blue = 50)
  val lightThemeTextColor = CommonColor(red = 102, green = 112, blue = 130)
  val darkThemeTextColor = CommonColor(red = 189, green = 189, blue = 189)

  val lightThemeDarkShadow = black.copy(alpha = 0.2)
  val lightThemeLightShadow = white.copy(alpha = 0.7)

  val darkThemeIntenseDropShadow = black.copy(alpha = 0.7)
  val lightThemeBackgroundGradient = gray.copy(alpha = 0.2)

  val darkThemeDarkShadow = black.copy(alpha = 0.4)
  val darkThemeLightShadow = CommonColor(red = 48, green = 52, blue = 58)

  val darkGradientStart = CommonColor(red = 50, green = 60, blue = 65)
  val darkGradientEnd = CommonColor(red = 25, green = 25, blue = 30)

  val orangeGradientStart = CommonColor(red = 255, green = 98, blue = 0)
  val orangeGradientEnd = CommonColor(red = 180, green = 49, blue = 0)
  val orangeGradientMiddle = CommonColor(red = 255, green = 180, blue = 0)

  val blueGradientStart = CommonColor(red = 66, green = 165, blue = 245)
  val blueGradientEnd = CommonColor(red = 13, green = 71, blue = 161)
  val blueGradientMiddle = CommonColor(red = 25, green = 118, blue = 210)

  val azurGradientStart = CommonColor(red = 127, green = 127, blue = 213)
  val azurGradientMiddle = CommonColor(red = 134, green = 168, blue = 231)
  val azurGradientEnd = CommonColor(red = 145, green = 234, blue = 228)
}