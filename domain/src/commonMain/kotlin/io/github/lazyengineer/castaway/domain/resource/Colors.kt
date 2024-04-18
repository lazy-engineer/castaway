package io.github.lazyengineer.castaway.domain.resource

object Colors {

  val Gray = CommonColor(red = 128, green = 128, blue = 128)
  val White = CommonColor(red = 255, green = 255, blue = 255)
  val Black = CommonColor(red = 0, green = 0, blue = 0)
  val DarkGray = CommonColor(red = 97, green = 97, blue = 97)
  val Red = CommonColor(red = 255, green = 0, blue = 0)

  object Material {

    val LightPrimary = BlueGradientStart
    val DarkPrimary = OrangeGradientStart
    val LightPrimaryVariant = BlueGradientMiddle
    val DarkPrimaryVariant = OrangeGradientMiddle
    val LightSecondary = BlueGradientEnd
    val DarkSecondary = OrangeGradientEnd
    val LightSecondaryVariant = BlueGradientEnd
    val DarkSecondaryVariant = OrangeGradientEnd

    val LightThemeBackground = White
    val DarkThemeBackground = Shark500
    val LightThemeTextColor = Black
    val DarkThemeTextColor = White

    val DarkThemeDarkShadow = Black.copy(alpha = 0.4)
    val DarkThemeLightShadow = Shark500
  }

  object Neumorphism {

    val LightThemeBackground = CommonColor(red = 225, green = 234, blue = 252)
    val DarkThemeBackground = Shark500
    val LightThemeTextColor = CommonColor(red = 102, green = 112, blue = 130)
    val DarkThemeTextColor = CommonColor(red = 189, green = 189, blue = 189)

    val LightThemeDarkShadow = Black.copy(alpha = 0.2)
    val LightThemeLightShadow = White.copy(alpha = 0.7)

    val DarkThemeIntenseDropShadow = Black.copy(alpha = 0.7)
    val LightThemeBackgroundGradient = Gray.copy(alpha = 0.2)

    val DarkThemeDarkShadow = Black.copy(alpha = 0.4)
    val DarkThemeLightShadow = CommonColor(red = 48, green = 52, blue = 58)

    val DarkGradientStart = CommonColor(red = 50, green = 60, blue = 65)
    val DarkGradientEnd = CommonColor(red = 25, green = 25, blue = 30)
  }

  val OrangeGradientStart = CommonColor(red = 255, green = 98, blue = 0)
  val OrangeGradientEnd = CommonColor(red = 180, green = 49, blue = 0)
  val OrangeGradientMiddle = CommonColor(red = 255, green = 180, blue = 0)

  val BlueGradientStart = CommonColor(red = 66, green = 165, blue = 245)
  val BlueGradientEnd = CommonColor(red = 13, green = 71, blue = 161)
  val BlueGradientMiddle = CommonColor(red = 25, green = 118, blue = 210)

  val AzurGradientStart = CommonColor(red = 127, green = 127, blue = 213)
  val AzurGradientMiddle = CommonColor(red = 134, green = 168, blue = 231)
  val AzurGradientEnd = CommonColor(red = 145, green = 234, blue = 228)

  val Shark50 = CommonColor(red = 229, green = 230, blue = 230) // #e5e6e6
  val Shark100 = CommonColor(red = 191, green = 192, blue = 194) // #bfc0c2
  val Shark200 = CommonColor(red = 148, green = 150, blue = 153) // #949699
  val Shark300 = CommonColor(red = 105, green = 108, blue = 112) // #696c70
  val Shark400 = CommonColor(red = 73, green = 77, blue = 81) // #494d51
  val Shark500 = CommonColor(red = 41, green = 45, blue = 50) // #292d32
  val Shark600 = CommonColor(red = 36, green = 40, blue = 45) // #24282d
  val Shark700 = CommonColor(red = 31, green = 34, blue = 38) // #1f2226
  val Shark800 = CommonColor(red = 25, green = 28, blue = 31) // #191c1f
  val Shark900 = CommonColor(red = 15, green = 17, blue = 19) // #0f1113
  val SharkA100 = CommonColor(red = 89, green = 145, blue = 255) // #5991ff
  val SharkA200 = CommonColor(red = 38, green = 111, blue = 255) // #266fff
  val SharkA400 = CommonColor(red = 0, green = 81, blue = 242) // #0051f2
  val SharkA700 = CommonColor(red = 0, green = 72, blue = 217) // #0048d9
}
