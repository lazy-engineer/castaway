package io.github.lazyengineer.castawayplayer.source

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class LocalDataSource private constructor(
  private val preferences: SharedPreferences,
  private val gson: Gson,
) {

  suspend fun saveRecentPlaylist(mediaDataList: List<MediaData>) {
	withContext(Dispatchers.IO) {
	  val mediaDataJson = gson.toJson(mediaDataList)

	  preferences.edit()
		.putString(RECENT_MEDIA_PLAYLIST, mediaDataJson)
		.apply()
	}
  }

  fun loadRecentPlaylist(): List<MediaData> {
	val mediaDataJson = preferences.getString(RECENT_MEDIA_PLAYLIST, null)

	val type: Type = object : TypeToken<List<MediaData>>() {}.type
	return gson.fromJson(mediaDataJson, type) ?: emptyList()
  }

  companion object {

	@Volatile
	private var INSTANCE: LocalDataSource? = null

	fun getInstance(
	  preferences: SharedPreferences,
	  gson: Gson
	): LocalDataSource {
	  return INSTANCE ?: synchronized(this) {
		INSTANCE ?: LocalDataSource(preferences, gson)
		  .also { INSTANCE = it }
	  }
	}
  }
}

const val PREFERENCES_NAME = "castaway_player_prefs"
private const val RECENT_MEDIA_PLAYLIST = "recent_media_playlist"
