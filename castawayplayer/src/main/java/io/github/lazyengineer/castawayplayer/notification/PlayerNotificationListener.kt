package io.github.lazyengineer.castawayplayer.notification

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import io.github.lazyengineer.castawayplayer.service.MediaPlayerService

class PlayerNotificationListener(
  private val mediaService: MediaPlayerService
) : PlayerNotificationManager.NotificationListener {

  private var isForegroundService = false

  override fun onNotificationPosted(
    notificationId: Int,
    notification: Notification,
    ongoing: Boolean
  ) {
    mediaService.apply {
      if (ongoing && !isForegroundService) {
        ContextCompat.startForegroundService(
          applicationContext,
          Intent(applicationContext, this::class.java)
        )

        startForeground(notificationId, notification)
        isForegroundService = true
      }
    }
  }

  override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
    mediaService.apply {
      stopForeground(true)
      isForegroundService = false
      stopSelf()
    }
  }
}
