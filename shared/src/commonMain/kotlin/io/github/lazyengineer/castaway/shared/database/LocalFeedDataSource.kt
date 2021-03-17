package io.github.lazyengineer.castaway.shared.database

import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.FeedInfo

interface LocalFeedDataSource {

  fun episodeFlow(episodeIds: List<String>): Flow<List<Episode>>
  fun episodeFlow(podcastUrl: String): Flow<List<Episode>>
  fun loadFeedInfo(feedUrl: String): Result<FeedInfo>
  fun loadFeed(feedUrl: String): Result<FeedData>
  fun loadEpisodes(episodeIds: List<String>): Result<List<Episode>>
  fun saveFeedData(feed: FeedInfo, episodes: List<Episode>): Result<FeedData>
  fun saveEpisode(episode: Episode): Result<Episode>
}
