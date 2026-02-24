package org.onereed.vigil

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

  @Inject lateinit var workerFactory: HiltWorkerFactory

  override val workManagerConfiguration: Configuration
    get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
    createNotificationChannel()
  }

  private fun createNotificationChannel() {
    val name = "Timer"
    val descriptionText = "Timer notifications"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel =
      NotificationChannel("timer_id", name, importance).apply { description = descriptionText }

    // Register the channel with the system

    val notificationManager: NotificationManager =
      getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
  }
}
