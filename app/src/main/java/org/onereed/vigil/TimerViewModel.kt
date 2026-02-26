package org.onereed.vigil

import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@HiltViewModel
class TimerViewModel @Inject constructor(private val workManager: WorkManager) : ViewModel() {

  // Observe progress using the Unique Work ID
  val timerProgress: Flow<Int?> =
    workManager.getWorkInfosForUniqueWorkFlow(UNIQUE_WORK_NAME).map {
      it.firstOrNull()?.progress?.getInt("seconds", 0)
    }

  fun startTimer() {
    val request =
      OneTimeWorkRequestBuilder<TimerWorker>()
        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        .build()

    workManager.enqueueUniqueWork(UNIQUE_WORK_NAME, ExistingWorkPolicy.REPLACE, request)
  }

  fun stopTimer() = workManager.cancelUniqueWork(UNIQUE_WORK_NAME)
}
