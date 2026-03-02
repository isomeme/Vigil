package org.onereed.vigil

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import org.onereed.vigil.common.sdkAtLeast

fun Context.createNotificationChannel() {
  val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  val channel =
    NotificationChannel(
        CHANNEL_ID,
        getString(R.string.timer_channel_name),
        NotificationManager.IMPORTANCE_HIGH,
      )
      .apply { description = getString(R.string.timer_channel_description) }

  notificationManager.createNotificationChannel(channel)
}

fun Context.createForegroundInfo(): ForegroundInfo {
  val notification = createNotification()

  return if (sdkAtLeast(UPSIDE_DOWN_CAKE)) {
    ForegroundInfo(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
  } else {
    ForegroundInfo(NOTIFICATION_ID, notification)
  }
}

fun Context.createNotification(): Notification =
  NotificationCompat.Builder(this, CHANNEL_ID)
    .setContentTitle(getString(R.string.timer_running))
    .setContentText("Text!")
    .setSmallIcon(R.drawable.ic_launcher_foreground)
    .setCategory(NotificationCompat.CATEGORY_ALARM)
    .setOngoing(true)
    .build()

const val UNIQUE_WORK_NAME = "unique_timer"

private const val CHANNEL_ID = "timer_id"

private const val NOTIFICATION_ID = 1
