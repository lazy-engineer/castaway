package io.github.lazyengineer.castaway.androidApp.ext

import androidx.compose.ui.graphics.Color
import io.github.lazyengineer.castaway.shared.resource.CommonColor

fun CommonColor.toColor(): Color {
  return Color(red = red, green = green, blue = blue, alpha = (alpha * 255).toInt())
}
