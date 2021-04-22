package io.github.lazyengineer.castaway.shared.entity

data class FeedData(
  val info: FeedInfo,
  val episodes: List<Episode>
)

data class FeedInfo(
  val url: String,
  val title: String,
  val imageUrl: String?,
)
