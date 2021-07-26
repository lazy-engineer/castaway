package io.github.lazyengineer.castaway.shared.resource

data class Color(
  val red: Int,
  val green: Int,
  val blue: Int,
  val alpha: Double = 1.0,
)

sealed class ThemedColor(open val color: Color) {
  data class Dark(override val color: Color) : ThemedColor(color)
  data class Light(override val color: Color) : ThemedColor(color)
}
