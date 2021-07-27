package io.github.lazyengineer.castaway.shared.resource

data class CommonColor(
  val red: Int,
  val green: Int,
  val blue: Int,
  val alpha: Double = 1.0,
)

sealed class ThemedColor(open val color: CommonColor) {
  data class Dark(override val color: CommonColor) : ThemedColor(color)
  data class Light(override val color: CommonColor) : ThemedColor(color)
}
