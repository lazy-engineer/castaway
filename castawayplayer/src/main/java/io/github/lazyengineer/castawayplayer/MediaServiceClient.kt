package io.github.lazyengineer.castawayplayer

import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import io.github.lazyengineer.castawayplayer.source.MediaData
import kotlinx.coroutines.flow.Flow

interface MediaServiceClient {

  fun playerState(): Flow<MediaServiceState>

  suspend fun subscribe(parentId: String, callback: SubscriptionCallback)
  fun unsubscribe(parentId: String, callback: SubscriptionCallback)
  fun prepare(playlist: List<MediaData>)

  fun dispatchMediaServiceEvent(event: MediaServiceEvent)
}


