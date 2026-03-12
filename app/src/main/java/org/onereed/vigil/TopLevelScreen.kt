package org.onereed.vigil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import org.onereed.vigil.common.hasPostNotificationsPermission
import timber.log.Timber

@Composable
fun TopLevelScreen() {
  Timber.d("TopLevelScreen start")

  val context = LocalContext.current

  var hasPerm by remember {
    mutableStateOf(context.hasPostNotificationsPermission())
  }

  LaunchedEffect(key1 = hasPerm) {
    Timber.d("Δ hasPerm -> %b", hasPerm)
  }

  LifecycleResumeEffect(Unit) {
    Timber.d("LifecycleResumeEffect")
    hasPerm = context.hasPostNotificationsPermission()
    onPauseOrDispose {}
  }

  if (hasPerm) {
    TimerScreen()
  } else {
    PermissionScreen(onPermissionGranted = {
      Timber.d("onPermissionGranted")
      hasPerm = true
    })
  }
}
