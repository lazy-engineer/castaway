package io.github.lazyengineer.castaway.androidApp.ext

import androidx.compose.ui.graphics.Color
import io.github.lazyengineer.castaway.shared.resource.CommonColor
import io.github.lazyengineer.castaway.shared.resource.ThemedColor

fun CommonColor.toColor(): Color {
  return Color(red = red, green = green, blue = blue, alpha = (alpha * 255).toInt())
}

fun ThemedColor.toColor(): Color {
  return Color(red = this.color.red, green = this.color.green, blue = this.color.blue, alpha = (this.color.alpha * 255).toInt())
}
