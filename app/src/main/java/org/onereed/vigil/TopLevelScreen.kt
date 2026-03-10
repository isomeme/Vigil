package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.onereed.vigil.common.hasPermission
import org.onereed.vigil.common.settingsIntent
import timber.log.Timber

@Composable
fun TopLevelScreen() {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  fun checkNotificationPermission(): Boolean {
    @SuppressLint("InlinedApi") // Permission check always safe.
    val hasPerm = context.hasPermission(POST_NOTIFICATIONS)
    Timber.d("checkNotificationPermission: %b", hasPerm)
    return hasPerm
  }

  var hasNotificationPermission by rememberSaveable {
    mutableStateOf(checkNotificationPermission())
  }

  Timber.d("TopLevelScreen: hasNotificationPermission = %b", hasNotificationPermission)

  fun updateNotificationPermission() {
    hasNotificationPermission = checkNotificationPermission()
    Timber.d("updateNotificationPermission: %b", hasNotificationPermission)
  }

  // When the user goes to the settings screen to grant notifications permission, this launcher
  // updates the permission state when they return.

  val intentLauncher =
    rememberLauncherForActivityResult(
      contract = ActivityResultContracts.StartActivityForResult(),
      onResult = { updateNotificationPermission() },
    )

  // When the user navigates away from the app then returns "manually", the observer managed by this
  // DisposableEffect updates the permission state.

  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == ON_RESUME) {
        updateNotificationPermission()
      }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
  }

  if (hasNotificationPermission) {
    TimerScreen()
  } else {
    PermissionScreen(
      onPermissionGranted = { hasNotificationPermission = true },
      onOpenSettings = { intentLauncher.launch(context.settingsIntent()) },
    )
  }
}
