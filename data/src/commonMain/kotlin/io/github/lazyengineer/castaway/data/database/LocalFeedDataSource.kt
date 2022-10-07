package io.github.lazyengineer.castaway.data.database

import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.FeedData
import io.github.lazyengineer.castaway.domain.entity.FeedInfo
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import kotlinx.coroutines.flow.Flow

interface LocalFeedDataSource {

  suspend fun loadFeedInfo(feedUrl: String): DataResult<FeedInfo>
  suspend fun loadFeed(feedUrl: String): DataResult<FeedData>
  suspend fun loadEpisodes(episodeIds: List<String>): DataResult<List<Episode>>
  suspend fun saveFeedData(feed: FeedInfo, episodes: List<Episode>): DataResult<FeedData>
  suspend fun saveEpisode(episode: Episode): DataResult<Episode>
  fun episodeFlow(episodeIds: List<String>): Flow<List<Episode>>
  fun episodeFlow(podcastUrl: String): Flow<List<Episode>>
}
