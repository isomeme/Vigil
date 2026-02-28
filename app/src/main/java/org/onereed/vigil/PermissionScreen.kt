package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.onereed.vigil.common.openSettings
import org.onereed.vigil.common.sdkAtLeast
import org.onereed.vigil.tool.VigilPreview

@Composable
fun PermissionScreen(onPermissionGranted: () -> Unit, onExit: () -> Unit) {
  if (!sdkAtLeast(Build.VERSION_CODES.TIRAMISU)) {
    onPermissionGranted()
    return
  }

  val context = LocalContext.current
  val activity =
    LocalActivity.current ?: throw IllegalStateException("PermissionScreen not in Activity")

  val launcher =
    rememberLauncherForActivityResult(
      contract = ActivityResultContracts.RequestPermission(),
      onResult = { isGranted ->
        if (isGranted) {
          onPermissionGranted()
        }
      },
    )

  // Request permission automatically when the screen is first shown.

  LaunchedEffect(Unit) { launcher.launch(POST_NOTIFICATIONS) }

  if (activity.shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
    // User has denied permission at least once. Explain why it's needed.

    StatelessPermissionScreen(
      explanationRes = R.string.notification_permission_rationale,
      okButtonAction = { launcher.launch(POST_NOTIFICATIONS) },
      exitButtonAction = onExit,
    )
  } else {
    // User has denied permission permanently, or it's the very first time. The LaunchedEffect
    // handles the first-time request. If we are still here, it means the user has permanently
    // denied it, so they must go to settings.

    StatelessPermissionScreen(
      explanationRes = R.string.notification_permission_use_settings,
      okButtonAction = context::openSettings,
      exitButtonAction = onExit,
    )
  }
}

@Composable
fun StatelessPermissionScreen(
  @StringRes explanationRes: Int,
  okButtonAction: () -> Unit,
  exitButtonAction: () -> Unit,
) {
  Column(
    modifier =
      Modifier.padding(all = 40.dp)
        .widthIn(max = 640.dp)
        .background(MaterialTheme.colorScheme.surfaceContainer)
        .padding(all = 15.dp)
        .verticalScroll(rememberScrollState())
  ) {
    Text(text = stringResource(explanationRes))

    Spacer(modifier = Modifier.height(20.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
      FilledTonalButton(onClick = exitButtonAction) {
        Text(text = stringResource(R.string.button_exit))
      }

      Spacer(modifier = Modifier.padding(horizontal = 20.dp))

      FilledTonalButton(onClick = okButtonAction) {
        Text(text = stringResource(R.string.button_ok))
      }
    }
  }
}

@Preview
@Composable
fun PermissionScreenPreview() {
  VigilPreview {
    StatelessPermissionScreen(
      explanationRes = R.string.notification_permission_rationale,
      okButtonAction = {},
      exitButtonAction = {},
    )
  }
}
