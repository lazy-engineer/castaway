package io.github.lazyengineer.castaway.data.database

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.github.lazyengineer.castaway.data.ext.toEpisode
import io.github.lazyengineer.castaway.data.ext.toEpisodeEntity
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.domain.entity.Episode
import io.github.lazyengineer.castaway.domain.entity.FeedData
import io.github.lazyengineer.castaway.domain.entity.FeedInfo
import io.github.lazyengineer.castaway.domain.entity.common.DataResult
import io.github.lazyengineer.castaway.domain.entity.common.DataResult.Error
import io.github.lazyengineer.castaway.domain.entity.common.DataResult.Success
import iogithublazyengineercastawaydb.EpisodeEntity
import iogithublazyengineercastawaydb.Podcast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FeedLocalDataSource constructor(private val database: CastawayDatabase, private val backgroundDispatcher: CoroutineDispatcher) :
  LocalFeedDataSource {

  override suspend fun loadFeedInfo(feedUrl: String): DataResult<FeedInfo> {
	return database.episodeQueries.transactionWithContext(backgroundDispatcher) {
	  try {
		val podcast = database.podcastQueries.selectByUrl(feedUrl).executeAsOne()

		Success(
		  FeedInfo(
			url = podcast.url,
			title = podcast.name,
			imageUrl = podcast.imageUrl,
		  )
		)
	  } catch (e: NullPointerException) {
		Error(e)
	  }
	}
  }

  override suspend fun loadFeed(feedUrl: String): DataResult<FeedData> {
	return database.episodeQueries.transactionWithContext(backgroundDispatcher) {
	  try {
		val podcast = database.podcastQueries.selectByUrl(feedUrl).executeAsOne()

		val episodes = database.episodeQueries.selectByPodcast(feedUrl).executeAsList().map {
		  it.toEpisode()
		}

		Success(
		  FeedData(
			FeedInfo(
			  url = podcast.url,
			  title = podcast.name,
			  imageUrl = podcast.imageUrl,
			),
			episodes = episodes
		  )
		)
	  } catch (e: NullPointerException) {
		Error(e)
	  }
	}
  }

  override suspend fun loadEpisodes(episodeIds: List<String>): DataResult<List<Episode>> {
	return database.episodeQueries.transactionWithContext(backgroundDispatcher) {
	  try {
		val episodes = database.episodeQueries.selectByIds(episodeIds).executeAsList().map {
		  it.toEpisode()
		}

		Success(episodes)
	  } catch (e: NullPointerException) {
		Error(e)
	  }
	}
  }

  override fun episodeFlow(episodeIds: List<String>): Flow<List<Episode>> {
	return database.episodeQueries.selectByIds(
	  episodeIds,
	  mapper = { id, title, subTitle, description, audioUrl, imageUrl, author, playbackPosition, episode, podcastUrl ->
		EpisodeEntity(
		  id = id,
		  title = title,
		  subTitle = subTitle,
		  description = description,
		  audioUrl = audioUrl,
		  imageUrl = imageUrl,
		  author = author,
		  playbackPosition = playbackPosition,
		  episode = episode,
		  podcastUrl = podcastUrl,
		).toEpisode()
	  }
	).asFlow()
	  .mapToList()
  }

  override fun episodeFlow(url: String): Flow<List<Episode>> {
	return database.episodeQueries.selectByPodcast(
	  url,
	  mapper = { id, title, subTitle, description, audioUrl, imageUrl, author, playbackPosition, episode, podcastUrl ->
		EpisodeEntity(
		  id = id,
		  title = title,
		  subTitle = subTitle,
		  description = description,
		  audioUrl = audioUrl,
		  imageUrl = imageUrl,
		  author = author,
		  playbackPosition = playbackPosition,
		  episode = episode,
		  podcastUrl = podcastUrl,
		).toEpisode()
	  }
	).asFlow()
	  .mapToList()
  }

  override suspend fun saveFeedData(feed: FeedInfo, episodes: List<Episode>): DataResult<FeedData> {
	val savedFeedInfoResult = insertFeedInfo(feed)
	val savedEpisodesResult = insertEpisodes(episodes)

	return when (savedFeedInfoResult) {
	  is Success -> {
		if (savedEpisodesResult.isNotEmpty()) {
		  Success(FeedData(info = savedFeedInfoResult.data, episodes = savedEpisodesResult))
		} else {
		  Error(Exception("Failed to save episodes"))
		}
	  }
	  is Error -> Error(Exception("Failed to save feed info"))
	}
  }

  private suspend fun insertFeedInfo(info: FeedInfo): DataResult<FeedInfo> {
	return try {
	  database.episodeQueries.transactionWithContext(backgroundDispatcher) {
		database.podcastQueries.insertPodcast(
		  Podcast(
			url = info.url,
			name = info.title,
			imageUrl = info.imageUrl,
		  )
		)

		Success(info)
	  }
	} catch (e: Exception) {
	  Error(e)
	}
  }

  private suspend fun insertEpisodes(episodes: List<Episode>): List<Episode> {
	return episodes.mapNotNull { episode ->
	  when (val savedEpisode = saveEpisode(episode)) {
		is Success -> savedEpisode.data
		is Error -> null
	  }
	}
  }

  override suspend fun saveEpisode(episode: Episode): DataResult<Episode> {
	return try {
	  database.episodeQueries.transactionWithContext(backgroundDispatcher) {
		val episodeEntity = episode.toEpisodeEntity()
		database.episodeQueries.insertEpisode(
		  episodeEntity
		)

		Success(episodeEntity.toEpisode())
	  }
	} catch (e: Exception) {
	  Error(e)
	}
  }

  private suspend fun <R> Transacter.transactionWithContext(
	coroutineContext: CoroutineContext,
	noEnclosing: Boolean = false,
	body: TransactionWithReturn<R>.() -> R
  ): R {
	return withContext(coroutineContext) {
	  this@transactionWithContext.transactionWithResult(noEnclosing) {
		body()
	  }
	}
  }
}
