package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.onereed.vigil.common.hasPermission
import org.onereed.vigil.common.settingsIntent
import timber.log.Timber

@Composable
fun TopLevelScreen() {
  Timber.d("TopLevelScreen")

  val context = LocalContext.current
  val activity = LocalActivity.current
  val lifecycleOwner = LocalLifecycleOwner.current

  var hasNotificationPermission by rememberSaveable {
    mutableStateOf(checkNotificationPermission(context))
  }

  // When the user goes to the settings screen to grant notifications permission, this launcher
  // updates the permission state when they return.

  val settingsLauncher =
    rememberLauncherForActivityResult(
      contract = ActivityResultContracts.StartActivityForResult(),
      onResult = { hasNotificationPermission = checkNotificationPermission(context) },
    )

  // When the user navigates away from the app then returns "manually", the observer managed by this
  // DisposableEffect updates the permission state.

  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == ON_RESUME) {
        hasNotificationPermission = checkNotificationPermission(context)
      }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
  }

  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Box(
      modifier = Modifier.fillMaxSize().padding(innerPadding),
      contentAlignment = Alignment.Center,
    ) {
      if (hasNotificationPermission) {
        TimerScreen()
      } else {
        PermissionScreen(
          onPermissionGranted = { hasNotificationPermission = true },
          onOpenSettings = { settingsLauncher.launch(context.settingsIntent()) },
        ) { activity?.finish() }
      }
    }
  }
}

@SuppressLint("InlinedApi") // Permission check always safe.
private fun checkNotificationPermission(context: Context): Boolean =
  context.hasPermission(POST_NOTIFICATIONS)
