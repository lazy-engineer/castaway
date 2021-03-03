package io.github.lazyengineer.castaway.shared.entity

import io.github.lazyengineer.castaway.shared.Image

data class FeedData(
  val url: String,
  val title: String,
  val imageUrl: String?,
  val image: Image?,
  val episodes: List<Episode>
)
