package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import org.onereed.shared.permission.hasPermission
import timber.log.Timber

@Composable
@SuppressLint("InlinedApi") // POST_NOTIFICATIONS protected by hasPerm processing
fun TopLevelScreen() {
  Timber.d("TopLevelScreen start")

  val context = LocalContext.current
  val activity = LocalActivity.current!!

  var hasPerm by remember { mutableStateOf(context.hasPermission(POST_NOTIFICATIONS)) }

  if (hasPerm) {
    TimerScreen()
  } else {
    @SuppressLint("InlinedApi") // POST_NOTIFICATIONS protected by hasPerm processing
    PermissionScreen(
      permission = POST_NOTIFICATIONS,
      rationaleText = stringResource(R.string.notification_permission_rationale),
      settingsText = stringResource(R.string.notification_permission_use_settings),
      onPermissionGranted = { hasPerm = true },
      onDismiss = activity::finish,
    )
  }

  // When the user changes app permissions using system settings while the app is closed, this
  // onResume check syncs to the new state.

  LifecycleResumeEffect(Unit) {
    hasPerm = context.hasPermission(POST_NOTIFICATIONS)
    onPauseOrDispose {}
  }

  LaunchedEffect(key1 = hasPerm) { Timber.d("Δ hasPerm -> $hasPerm") }

  Timber.d("TopLevelScreen end")
}
