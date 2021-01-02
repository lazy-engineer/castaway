package io.github.lazyengineer.castaway.shared.database

import io.github.lazyengineer.castaway.db.CastawayDatabase
import io.github.lazyengineer.castaway.shared.common.Result
import io.github.lazyengineer.castaway.shared.entity.Episode
import io.github.lazyengineer.castaway.shared.entity.FeedData
import io.github.lazyengineer.castaway.shared.entity.PlaybackPosition
import iogithublazyengineercastawaydb.Episode as SqlEpisode

class FeedLocalDataSource constructor(private val database: CastawayDatabase) :
    LocalFeedDataSource {

    override suspend fun loadFeed(feedUrl: String): Result<FeedData> {
        val feed: FeedData = database.episodeQueries.transactionWithResult {
            val podcast = database.podcastQueries.selectByUrl(feedUrl).executeAsOne()

            val episodes = database.episodeQueries.selectByPodcast(feedUrl).executeAsList().map {
                Episode(
                    id = it.id,
                    title = it.title,
                    subTitle = it.subTitle,
                    description = it.description,
                    audioUrl = it.audioUrl,
                    imageUrl = it.imageUrl,
                    author = it.author,
                    playbackPosition = it.playbackPosition ?: PlaybackPosition(0),
                    isPlaying = it.isPlaying ?: false,
                    podcastUrl = it.podcastUrl,
                )
            }

            FeedData(url = podcast.url, title = podcast.name, episodes = episodes)
        }

        return Result.Success(feed)
    }

    override suspend fun saveFeedData(feed: FeedData): Result<FeedData> {
        val savedFeed: FeedData = database.episodeQueries.transactionWithResult {
            database.podcastQueries.insertPodcast(feed.url, feed.title)
            feed.episodes.forEach {
                database.episodeQueries.insertEpisode(
                    SqlEpisode(
                        id = it.id,
                        title = it.title,
                        subTitle = it.subTitle,
                        description = it.description,
                        audioUrl = it.audioUrl,
                        imageUrl = it.imageUrl,
                        author = it.author,
                        playbackPosition = it.playbackPosition,
                        isPlaying = it.isPlaying,
                        podcastUrl = feed.url,
                    )
                )
            }

            feed
        }

        return Result.Success(savedFeed)
    }

    override suspend fun saveEpisode(episode: Episode): Result<Episode> {
        val savedEpisode: Episode = database.episodeQueries.transactionWithResult {
            database.episodeQueries.insertEpisode(
                SqlEpisode(
                    id = episode.id,
                    title = episode.title,
                    subTitle = episode.subTitle,
                    description = episode.description,
                    audioUrl = episode.audioUrl,
                    imageUrl = episode.imageUrl,
                    author = episode.author,
                    playbackPosition = episode.playbackPosition,
                    isPlaying = episode.isPlaying,
                    podcastUrl = episode.podcastUrl,
                )
            )

            episode
        }

        return Result.Success(savedEpisode)
    }
}