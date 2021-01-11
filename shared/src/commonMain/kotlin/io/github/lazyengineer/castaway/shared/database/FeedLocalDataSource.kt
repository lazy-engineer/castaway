package io.github.lazyengineer.castaway.shared.database

import co.touchlab.stately.ensureNeverFrozen
import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition
import iogithublazyengineercastawaydb.EpisodeEntity

class FeedLocalDataSource constructor(private val database: CastawayDatabase) :
    LocalFeedDataSource {

    init {
        ensureNeverFrozen()
    }

    override suspend fun loadFeed(feedUrl: String): Result<FeedData> {
        val feed: FeedData = database.episodeQueries.transactionWithResult {
            val podcast = database.podcastQueries.selectByUrl(feedUrl).executeAsOne()

            val episodes = database.episodeQueries.selectByPodcast(feedUrl).executeAsList().map {
                it.toEpisode()
            }

            FeedData(url = podcast.url, title = podcast.name, episodes = episodes)
        }

        return Result.Success(feed)
    }

    override suspend fun loadEpisodes(episodeIds: List<String>): Result<List<Episode>> {
        val episodes: List<Episode> = database.episodeQueries.transactionWithResult {
            database.episodeQueries.selectByIds(episodeIds).executeAsList().map {
                it.toEpisode()
            }
        }

        return Result.Success(episodes)
    }

    override suspend fun saveFeedData(feed: FeedData): Result<FeedData> {
        val savedFeed: FeedData = database.episodeQueries.transactionWithResult {
            database.podcastQueries.insertPodcast(feed.url, feed.title)
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
            database.episodeQueries.insertEpisode(
                episode.toEpisodeEntity()
            )

            episode
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
            author = this.author,
            playbackPosition = this.playbackPosition ?: PlaybackPosition(0),
            isPlaying = this.isPlaying ?: false,
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
            author = this.author,
            playbackPosition = this.playbackPosition,
            isPlaying = this.isPlaying,
            podcastUrl = this.podcastUrl,
        )
    }
}