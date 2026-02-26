package org.onereed.vigil

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo

fun Context.createNotificationChannel() {
  val notificationManager: NotificationManager =
    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  val channel =
    NotificationChannel(
        CHANNEL_ID,
        getString(R.string.timer_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT,
      )
      .apply { description = getString(R.string.timer_channel_description) }

  notificationManager.createNotificationChannel(channel)
}

fun Context.createForegroundInfo(): ForegroundInfo {
  val notification =
    NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle(getString(R.string.timer_running))
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setOngoing(true)
      .build()

  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
    ForegroundInfo(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
  } else {
    ForegroundInfo(NOTIFICATION_ID, notification)
  }
}

const val UNIQUE_WORK_NAME = "unique_timer"

private const val CHANNEL_ID = "timer_id"

private const val NOTIFICATION_ID = 1
