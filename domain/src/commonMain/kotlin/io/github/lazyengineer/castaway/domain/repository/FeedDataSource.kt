package io.github.lazyengineer.castaway.domain.repository

import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.FeedData
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import kotlinx.coroutines.flow.Flow

interface FeedDataSource {

  suspend fun saveFeed(feed: FeedData): DataResult<FeedData>
  suspend fun saveEpisode(episode: Episode): DataResult<Episode>
  suspend fun fetchXml(url: String): DataResult<String>
  suspend fun loadLocally(url: String): DataResult<FeedData>
  suspend fun loadEpisodes(episodeIds: List<String>): DataResult<List<Episode>>
  fun episodeFlow(episodeIds: List<String>): Flow<DataResult<Episode>>
  fun episodeFlow(podcastUrl: String): Flow<DataResult<Episode>>
}
