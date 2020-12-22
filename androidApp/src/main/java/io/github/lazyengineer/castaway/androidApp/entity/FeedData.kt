package io.github.lazyengineer.castaway.androidApp.entity

import io.github.lazyengineer.castaway.androidApp.ArgumentsKt
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedData(val url: String, val title: String, val episodes: List<Episode>) : ArgumentsKt
