package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import org.onereed.vigil.common.hasPermission
import timber.log.Timber

@Composable
fun TopLevelScreen() {
  Timber.d("TopLevelScreen start")

  val context = LocalContext.current

  @SuppressLint("InlinedApi") // Permission check always safe.
  fun checkPerm() = context.hasPermission(POST_NOTIFICATIONS)

  var hasPerm by remember {
    val initPerm = checkPerm()
    Timber.d("remember: initPerm = %b", initPerm)
    mutableStateOf(initPerm)
  }

  LifecycleResumeEffect(Unit) {
    hasPerm = checkPerm()
    Timber.d("LifecycleResumeEffect: hasPerm = %b", hasPerm)
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
