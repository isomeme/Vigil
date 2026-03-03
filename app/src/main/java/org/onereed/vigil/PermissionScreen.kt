package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.onereed.vigil.common.sdkAtLeast
import org.onereed.vigil.tool.ThemePreviews
import org.onereed.vigil.tool.VigilPreview
import timber.log.Timber

@Composable
fun PermissionScreen(
  onPermissionGranted: () -> Unit,
  onOpenSettings: () -> Unit,
  onExit: () -> Unit,
) {
  if (!sdkAtLeast(TIRAMISU)) {
    onPermissionGranted()
    return
  }

  Timber.d("PermissionScreen")

  val activity =
    LocalActivity.current ?: throw IllegalStateException("PermissionScreen not in Activity")

  // We increment requestCount to trigger recomposition after each time a launched permission
  // request reports that the user denied permission.

  var requestCount by rememberSaveable { mutableIntStateOf(0) }
  val hasRequestedPermission by remember { derivedStateOf { requestCount > 0 } }
  val shouldShowRationale =
    remember(requestCount) { activity.shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) }

  Timber.d(
    "requestCount: %d, hasRequestedPermission: %b, shouldShowRationale: %b",
    requestCount,
    hasRequestedPermission,
    shouldShowRationale,
  )

  val permissionRequestLauncher =
    rememberLauncherForActivityResult(
      contract = ActivityResultContracts.RequestPermission(),
      onResult = { isGranted ->
        if (isGranted) {
          onPermissionGranted()
        } else {
          requestCount += 1
        }
      },
    )

  // Request permission automatically when the screen is first shown.

  LaunchedEffect(Unit) { permissionRequestLauncher.launch(POST_NOTIFICATIONS) }

  if (hasRequestedPermission) {
    if (shouldShowRationale) {
      // The user has denied permission once when shown the grant-permission dialog without context.
      // Explain why we need the permission and provide an opportunity to use the dialog again to
      // grant permission.

      StatelessPermissionScreen(
        explanationRes = R.string.notification_permission_rationale,
        okButtonAction = { permissionRequestLauncher.launch(POST_NOTIFICATIONS) },
        exitButtonAction = onExit,
      )
    } else {
      // If we have requested permission and shouldShowRationale is false, that means the system
      // will no longer show them the normal grant-permission dialog. Instead, they must go to the
      // app's system settings and grant permission "manually".

      StatelessPermissionScreen(
        explanationRes = R.string.notification_permission_use_settings,
        okButtonAction = onOpenSettings,
        exitButtonAction = onExit,
      )
    }
  }

  // If we have not requested permission, we leave the screen blank. A system permission dialog may
  // appear on top of this from the LaunchedEffect. Once that runs, either this screen becomes
  // irrelevant, or the states of hasRequestedPermission and shouldShowRationale will lead to one of
  // the branches being displayed.
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

@ThemePreviews
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
