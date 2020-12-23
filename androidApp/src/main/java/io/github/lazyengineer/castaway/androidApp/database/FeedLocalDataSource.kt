package io.github.lazyengineer.castaway.androidApp.database

import android.content.SharedPreferences
import com.google.gson.Gson
import io.github.lazyengineer.castaway.androidApp.common.Result
import io.github.lazyengineer.castaway.androidApp.common.Result.Error
import io.github.lazyengineer.castaway.androidApp.common.Result.Success
import io.github.lazyengineer.castaway.androidApp.entity.Episode
import io.github.lazyengineer.castaway.androidApp.entity.FeedData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeedLocalDataSource private constructor(
	private val preferences: SharedPreferences,
	private val gson: Gson,
) : LocalFeedDataSource {

	override suspend fun fetchFeed(feedUrl: String): Result<FeedData> {
		val feedJson = preferences.getString(feedUrl, null)
		val storedFeed = gson.fromJson(feedJson, FeedData::class.java)
		return if (storedFeed != null) {
			Success(storedFeed)
		} else {
			Error(Exception("Failed to get Feed locally"))
		}
	}

	override suspend fun saveFeedData(feed: FeedData): Result<FeedData> {
		withContext(Dispatchers.IO) {
			val feedJson = gson.toJson(feed)

			preferences.edit()
				.putString(feed.url, feedJson)
				.apply()
		}

		return Success(feed)
	}

	override suspend fun saveEpisode(episode: Episode): Result<Episode> {
		val url = "https://feeds.feedburner.com/blogspot/androiddevelopersbackstage"
		return when (val localFeed = fetchFeed(url)) {
			is Success -> {
				val saveEpisode = localFeed.data.episodes.find {
					it.id == episode.id
				}

				val updatedEpisodes = localFeed.data.episodes.mapNotNull {
					if (it.id == episode.id) {
						saveEpisode
					} else {
						it
					}
				}

				when (saveFeedData(feed = localFeed.data.copy(episodes = updatedEpisodes))) {
					is Success -> Success(episode)
					is Error -> Error(Exception("Failed to save Feed"))
				}
			}
			is Error -> return Error(Exception("No Feed saved yet"))
		}
	}

	companion object {

		@Volatile
		private var INSTANCE: FeedLocalDataSource? = null

		fun getInstance(preferences: SharedPreferences, gson: Gson): FeedLocalDataSource {
			return INSTANCE ?: synchronized(this) {
				INSTANCE ?: FeedLocalDataSource(preferences, gson)
					.also { INSTANCE = it }
			}
		}
	}
}

const val CASTAWAY_PREFERENCES_NAME = "castaway_prefs"
