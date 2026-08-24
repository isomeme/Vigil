package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.onereed.shared.permission.PermissionGate
import timber.log.Timber

@Composable
fun TopLevelScreen() {
  Timber.d("Start")

  @SuppressLint("InlinedApi") // POST_NOTIFICATIONS protected by PermissionGate logic
  PermissionGate(
    permissions = listOf(POST_NOTIFICATIONS),
    grantButtonLabel = "Allow notifications",
    rationaleTitle = "Notifications permission required",
    rationaleDescription = stringResource(R.string.notification_permission_rationale),
    useSettingsTitle = "Notifications permission required",
    useSettingsDescription = stringResource(R.string.notification_permission_use_settings),
  ) {
    TimerScreen()
  }

  Timber.d("End")
}
