package io.github.lazyengineer.castaway.androidApp.ext

import androidx.compose.ui.graphics.Color
import io.github.lazyengineer.castaway.shared.resource.Color as SharedColor

fun SharedColor.toColor(): Color {
  return Color(red = red, green = green, blue = blue, alpha = (alpha * 255).toInt())
}
