package io.github.lazyengineer.castaway.shared.resource

data class Color(
  val red: Int,
  val green: Int,
  val blue: Int,
  val alpha: Double = 1.0,
)

sealed interface ThemedColor {
  data class Dark(val color: Color) : ThemedColor
  data class Light(val color: Color) : ThemedColor
}
