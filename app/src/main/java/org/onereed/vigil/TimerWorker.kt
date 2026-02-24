package org.onereed.vigil

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class TimerWorker
@AssistedInject
constructor(
  @Assisted appContext: Context,
  @Assisted workerParams: WorkerParameters,
  // Add any Hilt-injected dependencies here (e.g., a Repository)
) : CoroutineWorker(appContext, workerParams) {

  override suspend fun doWork(): Result {
    for (i in 0..100) {
      if (isStopped) break
      delay(1000L)
      // Update progress for UI observation
      setProgress(workDataOf("seconds" to i))
    }

    return Result.success()
  }

  override suspend fun getForegroundInfo(): ForegroundInfo {
    val channelId = "timer_id"

    // Create notification channel if needed (Oreo+)
    val notification =
      NotificationCompat.Builder(applicationContext, channelId)
        .setContentTitle("WorkManager Timer")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setOngoing(true)
        .build()

    // Specify foregroundServiceType for Android 14+ compat
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        ForegroundInfo(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
    } else {
        ForegroundInfo(1, notification)
    }
  }
}
