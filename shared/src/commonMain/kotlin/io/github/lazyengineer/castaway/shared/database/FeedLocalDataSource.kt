package io.github.lazyengineer.castaway.shared.database

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition
import io.github.lazyengineer.castaway.shared.fromNativeImage
import io.github.lazyengineer.castaway.shared.toNativeImage
import iogithublazyengineercastawaydb.EpisodeEntity
import iogithublazyengineercastawaydb.Podcast

class FeedLocalDataSource constructor(private val database: CastawayDatabase) :
  LocalFeedDataSource {

  init {
	ensureNeverFrozen()
  }

  override suspend fun loadFeed(feedUrl: String): Result<FeedData> {
	return database.episodeQueries.transactionWithResult {
	  try {
		val podcast = database.podcastQueries.selectByUrl(feedUrl).executeAsOne()

		val episodes = database.episodeQueries.selectByPodcast(feedUrl).executeAsList().map {
		  it.toEpisode()
		}

		Result.Success(
		  FeedData(
			url = podcast.url,
			title = podcast.name,
			imageUrl = podcast.imageUrl,
			image = podcast.image?.toNativeImage(),
			episodes = episodes
		  )
		)
	  } catch (e: NullPointerException) {
		Result.Error(e)
	  }
	}
  }

  override suspend fun loadEpisodes(episodeIds: List<String>): Result<List<Episode>> {
	return database.episodeQueries.transactionWithResult {
	  try {
		val episodes = database.episodeQueries.selectByIds(episodeIds).executeAsList().map {
		  it.toEpisode()
		}

		Result.Success(episodes)
	  } catch (e: NullPointerException) {
		Result.Error(e)
	  }
	}
  }

  override suspend fun saveFeedData(feed: FeedData): Result<FeedData> {
	val savedFeed: FeedData = database.episodeQueries.transactionWithResult {
	  database.podcastQueries.insertPodcast(
		Podcast(
		  url = feed.url,
		  name = feed.title,
		  imageUrl = feed.imageUrl,
		  image = feed.image?.fromNativeImage()
		)
	  )
	  feed.episodes.forEach {
		database.episodeQueries.insertEpisode(
		  it.toEpisodeEntity()
		)
	  }

	  feed
	}

	return Result.Success(savedFeed)
  }

  override suspend fun saveEpisode(episode: Episode): Result<Episode> {
	val savedEpisode: Episode = database.episodeQueries.transactionWithResult {
	  val episodeEntity = episode.toEpisodeEntity()
	  database.episodeQueries.insertEpisode(
		episodeEntity
	  )

	  episodeEntity.toEpisode()
	}

	return Result.Success(savedEpisode)
  }

  private fun EpisodeEntity.toEpisode(): Episode {
	return Episode(
	  id = this.id,
	  title = this.title,
	  subTitle = this.subTitle,
	  description = this.description,
	  audioUrl = this.audioUrl,
	  imageUrl = this.imageUrl,
	  image = this.image?.toNativeImage(),
	  author = this.author,
	  playbackPosition = this.playbackPosition ?: PlaybackPosition(0),
	  episode = this.episode.toInt(),
	  podcastUrl = this.podcastUrl,
	)
  }

  private fun Episode.toEpisodeEntity(): EpisodeEntity {
	return EpisodeEntity(
	  id = this.id,
	  title = this.title,
	  subTitle = this.subTitle,
	  description = this.description,
	  audioUrl = this.audioUrl,
	  imageUrl = this.imageUrl,
	  image = this.image?.fromNativeImage(),
	  author = this.author,
	  playbackPosition = this.playbackPosition,
	  episode = this.episode.toLong(),
	  podcastUrl = this.podcastUrl,
	)
  }
}