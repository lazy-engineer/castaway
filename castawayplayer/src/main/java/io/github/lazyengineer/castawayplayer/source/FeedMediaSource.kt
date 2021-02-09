package io.github.lazyengineer.castawayplayer.source

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FeedMediaSource private constructor(
	private val localDataSource: LocalDataSource,
) : AbstractMediaSource() {

  private val currentPlaylistFlow = MutableStateFlow(emptyList<MediaData>())

  override fun iterator(): Iterator<MediaData> = currentPlaylistFlow.value.iterator()
  override suspend fun fetch() = prepare(localDataSource.loadRecentPlaylist())
  override suspend fun flow(): StateFlow<List<MediaData>> = currentPlaylistFlow
  override suspend fun saveRecent() = localDataSource.saveRecentPlaylist(currentPlaylistFlow.value)

  private fun playlistState() {
	val playlist = currentPlaylistFlow.value

	state = if (playlist.isNotEmpty()) {
	  STATE_INITIALIZED
	} else {
	  STATE_ERROR
	}
  }

  fun prepare(playlist: () -> List<MediaData>) {
	prepare(playlist())
  }

  fun prepare(playlist: List<MediaData>) {
	currentPlaylistFlow.value = playlist
	playlistState()
  }

  companion object {

	@Volatile
	private var INSTANCE: FeedMediaSource? = null

	fun getInstance(localDataSource: LocalDataSource): FeedMediaSource {
	  return INSTANCE ?: synchronized(this) {
		INSTANCE ?: FeedMediaSource(localDataSource = localDataSource)
		  .also { INSTANCE = it }
	  }
	}
  }
}
