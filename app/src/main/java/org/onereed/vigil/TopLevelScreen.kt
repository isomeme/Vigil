package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import org.onereed.shared.permission.PermissionOrDie
import timber.log.Timber

@Composable
fun TopLevelScreen() {
  Timber.d("TopLevelScreen start")

  @SuppressLint("InlinedApi")
  PermissionOrDie(POST_NOTIFICATIONS)
  TimerScreen()
}
