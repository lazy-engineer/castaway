package io.github.lazyengineer.castawayplayer.notification

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener
import io.github.lazyengineer.castawayplayer.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val NOTIFICATION_CHANNEL_ID = "castawayplayer"
private const val NOTIFICATION_ID = 0xb111

class MediaNotificationManager(
  private val context: Context,
  private val imageLoader: ImageLoader,
  private val notificationIconResId: Int,
  sessionToken: MediaSessionCompat.Token,
  notificationListener: NotificationListener
) {

  private val notificationManager: PlayerNotificationManager
  private val serviceJob = SupervisorJob()
  private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

  init {
    val mediaController = MediaControllerCompat(context, sessionToken)

    notificationManager = PlayerNotificationManager.Builder(
      context,
      NOTIFICATION_ID, NOTIFICATION_CHANNEL_ID
    )
      .setChannelNameResourceId(R.string.notification_channel_name)
      .setChannelDescriptionResourceId(R.string.notification_channel_description)
      .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
      .setNotificationListener(notificationListener)
      .build().apply {
        setMediaSessionToken(sessionToken)
        setSmallIcon(notificationIconResId)
      }
  }

  fun hideNotification() {
    notificationManager.setPlayer(null)
  }

  fun showNotificationForPlayer(player: Player) {
    notificationManager.setPlayer(player)
  }

  private inner class DescriptionAdapter(private val controller: MediaControllerCompat) :
    PlayerNotificationManager.MediaDescriptionAdapter {

    var currentIconUri: Uri? = null
    var currentBitmap: Bitmap? = null

    override fun createCurrentContentIntent(player: Player): PendingIntent? =
      controller.sessionActivity

    override fun getCurrentContentText(player: Player) =
      controller.metadata?.description?.subtitle.toString()

    override fun getCurrentContentTitle(player: Player) =
      controller.metadata?.description?.title.toString()

    override fun getCurrentLargeIcon(
      player: Player,
      callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
      val iconUri = controller.metadata?.description?.iconUri
      return if (currentIconUri != iconUri || currentBitmap == null) {

        // Cache the bitmap for the current song so that successive calls to
        // `getCurrentLargeIcon` don't cause the bitmap to be recreated.
        currentIconUri = iconUri
        serviceScope.launch {
          currentBitmap = iconUri?.let {
            resolveUriAsBitmap(it)
          }
          currentBitmap?.let { callback.onBitmap(it) }
        }
        null
      } else {
        currentBitmap
      }
    }

    private suspend fun resolveUriAsBitmap(uri: Uri): Bitmap? {
      return withContext(Dispatchers.IO) {
        val request = ImageRequest.Builder(context)
          .data(uri)
          .placeholder(R.drawable.default_art)
          .size(NOTIFICATION_LARGE_ICON_SIZE)
          .build()

        val result = imageLoader.execute(request)

        result.drawable?.let { (it as BitmapDrawable).bitmap }
      }
    }
  }
}

const val NOTIFICATION_LARGE_ICON_SIZE = 144
