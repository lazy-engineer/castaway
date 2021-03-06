package io.github.lazyengineer.castaway.shared.repository

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import kotlinx.coroutines.flow.Flow

interface FeedDataSource {

  suspend fun saveFeed(feed: FeedData): Result<FeedData>
  suspend fun saveEpisode(episode: Episode): Result<Episode>
  suspend fun fetchXml(url: String): Result<String>
  suspend fun loadLocally(url: String): Result<FeedData>
  suspend fun loadEpisodes(episodeIds: List<String>): Result<List<Episode>>
  fun episodeFlow(episodeIds: List<String>): Flow<Result<Episode>>
  fun episodeFlow(podcastUrl: String): Flow<Result<Episode>>
}
