package io.github.lazyengineer.castaway.shared.database

import co.touchlab.stately.freeze
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.common.Result.Error
import io.github.lazyengineer.castaway.shared.common.Result.Success
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.FeedInfo
import io.github.lazyengineer.castaway.shared.ext.toEpisode
import io.github.lazyengineer.castaway.shared.ext.toEpisodeEntity
import iogithublazyengineercastawaydb.EpisodeEntity
import iogithublazyengineercastawaydb.Podcast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FeedLocalDataSource constructor(private val database: CastawayDatabase, private val backgroundDispatcher: CoroutineDispatcher) :
  LocalFeedDataSource {

  init {
	freeze()
  }

  override suspend fun loadFeedInfo(feedUrl: String): Result<FeedInfo> {
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

  override suspend fun loadFeed(feedUrl: String): Result<FeedData> {
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

  override suspend fun loadEpisodes(episodeIds: List<String>): Result<List<Episode>> {
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

  override fun episodeFlow(podcastUrl: String): Flow<List<Episode>> {
	return database.episodeQueries.selectByPodcast(
	  podcastUrl,
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

  override suspend fun saveFeedData(feed: FeedInfo, episodes: List<Episode>): Result<FeedData> {
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

  private suspend fun insertFeedInfo(info: FeedInfo): Result<FeedInfo> {
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

  override suspend fun saveEpisode(episode: Episode): Result<Episode> {
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
