package org.onereed.vigil

import android.content.Context
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
    // Explicitly call setForeground() to notify UI at the beginning of work.
    // This is the recommended practice for expedited workers.

    setForeground(getForegroundInfo())

    for (i in 0..1_000_000) {
      if (isStopped) break
      delay(1000L)
      // Update progress for UI observation
      setProgress(workDataOf("seconds" to i))
    }

    return Result.success()
  }

  override suspend fun getForegroundInfo(): ForegroundInfo =
    applicationContext.createForegroundInfo()
}
