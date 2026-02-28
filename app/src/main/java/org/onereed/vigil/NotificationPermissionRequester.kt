package org.onereed.vigil

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun NotificationPermissionRequester(onPermissionGranted: () -> Unit) {
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
    onPermissionGranted()
    return
  }

  val context = LocalContext.current
  var hasNotificationPermission by remember {
    mutableStateOf(
      ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED
    )
  }

  val permissionLauncher =
    rememberLauncherForActivityResult(
      contract = ActivityResultContracts.RequestPermission(),
      onResult = { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
          onPermissionGranted()
        }
      },
    )

  LaunchedEffect(key1 = true) {
    if (!hasNotificationPermission) {
      permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
  }
}
