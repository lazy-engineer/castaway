package io.github.lazyengineer.castaway.shared.entity

data class FeedData(val url: String, val title: String, val image: String?, val episodes: List<Episode>)
