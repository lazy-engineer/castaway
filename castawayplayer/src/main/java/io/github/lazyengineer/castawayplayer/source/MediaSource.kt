package io.github.lazyengineer.castawayplayer.source

import androidx.annotation.IntDef
import kotlinx.coroutines.flow.StateFlow

interface MediaSource : Iterable<MediaData> {

	suspend fun fetch()

	suspend fun saveRecent()

	suspend fun flow(): StateFlow<List<MediaData>>

	fun whenReady(performAction: (Boolean) -> Unit): Boolean
}

@IntDef(
	STATE_CREATED,
	STATE_INITIALIZING,
	STATE_INITIALIZED,
	STATE_ERROR
)
@Retention(AnnotationRetention.SOURCE)
annotation class State

/**
 * State indicating the source was created, but no initialization has performed.
 */
const val STATE_CREATED = 1

/**
 * State indicating initialization of the source is in progress.
 */
const val STATE_INITIALIZING = 2

/**
 * State indicating the source has been initialized and is ready to be used.
 */
const val STATE_INITIALIZED = 3

/**
 * State indicating an error has occurred.
 */
const val STATE_ERROR = 4

abstract class AbstractMediaSource : MediaSource {

	@State
	var state: Int = STATE_CREATED
		set(value) {
			if (value == STATE_INITIALIZED || value == STATE_ERROR) {
				synchronized(onReadyListeners) {
					field = value
					onReadyListeners.forEach { listener ->
						listener(state == STATE_INITIALIZED)
					}
				}
			} else {
				field = value
			}
		}

	private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

	override fun whenReady(performAction: (Boolean) -> Unit): Boolean =
		when (state) {
			STATE_CREATED, STATE_INITIALIZING -> {
				onReadyListeners += performAction
				false
			}
			else -> {
				performAction(state != STATE_ERROR)
				true
			}
		}
}
