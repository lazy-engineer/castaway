package io.github.lazyengineer.castaway.androidApp.player

import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import io.github.lazyengineer.castawayplayer.MediaServiceClient
import io.github.lazyengineer.castawayplayer.service.Constants

class SubscribeToPlayerUseCase(
  private val castawayPlayer: MediaServiceClient
) {

  suspend operator fun invoke() {
	castawayPlayer.subscribe(Constants.MEDIA_ROOT_ID, subscriptionCallback)
  }

  private val subscriptionCallback = object : SubscriptionCallback() {
	override fun onChildrenLoaded(
	  parentId: String, children: MutableList<MediaItem>
	) {
	  super.onChildrenLoaded(parentId, children)
	}
  }
}
