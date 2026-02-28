package org.onereed.vigil

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TopLevelScreen() {
  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionState =
          rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

        if (notificationPermissionState.status.isGranted) {
          TimerScreen()
        } else {
          PermissionScreen(notificationPermissionState)
        }
      } else {
        TimerScreen()
      }
    }
  }
}
