package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.onereed.vigil.common.hasPermission

@Composable
fun TopLevelScreen() {
  val context = LocalContext.current
  val activity = LocalActivity.current

  @SuppressLint("InlinedApi") // hasPermission is safe for all sdk versions
  var hasNotificationPermission by rememberSaveable {
    mutableStateOf(context.hasPermission(POST_NOTIFICATIONS))
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
          onExit = { activity?.finish() },
        )
      }
    }
  }
}
