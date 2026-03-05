package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.onereed.vigil.common.sdkAtLeast
import org.onereed.vigil.tool.DarkPreview
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

  fun requestNotificationPermission() = permissionRequestLauncher.launch(POST_NOTIFICATIONS)

  if (shouldShowRationale) {
    // The user has denied the permission previously, but Android will still show them the normal
    // system permission dialog. Explain why we need the permission, and provide an opportunity to
    // grant it.

    StatelessPermissionScreen(
      explanationRes = R.string.notification_permission_rationale,
      okButtonAction = { requestNotificationPermission() },
      exitButtonAction = onExit,
    )
  } else if (hasRequestedPermission) {
    // Android will no longer show the user the normal system permission dialog. Explain that they
    // can grant the permission in the app's settings, and provide an opportunity to go there.

    StatelessPermissionScreen(
      explanationRes = R.string.notification_permission_use_settings,
      okButtonAction = onOpenSettings,
      exitButtonAction = onExit,
    )
  } else {
    // The user has not previously been asked to grant the permission. Show them the normal system
    // permission dialog over an otherwise blank screen. The launch callback is guaranteed to
    // either (1) trigger a TopLevelScreen recomposition that makes this screen irrelevant, or
    // (2) update a state variable that triggers a recomposition of this screen with
    // hasRequestedPermission set to true, making this block unreachable.

    LaunchedEffect(Unit) { requestNotificationPermission() }
  }
}

@Composable
fun StatelessPermissionScreen(
  @StringRes explanationRes: Int,
  okButtonAction: () -> Unit,
  exitButtonAction: () -> Unit,
) {
  // We set the two onDismissRequest callbacks to empty lambdas to make the dialog non-cancelable.

  Dialog(
    onDismissRequest = {},
    properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false),
  ) {
    AlertDialog(
      onDismissRequest = {},
      title = { Text(text = stringResource(R.string.notification_permission_dialog_title)) },
      text = { Text(text = stringResource(explanationRes)) },
      confirmButton = {
        TextButton(onClick = okButtonAction) { Text(text = stringResource(R.string.button_ok)) }
      },
      dismissButton = {
        TextButton(onClick = exitButtonAction) { Text(text = stringResource(R.string.button_exit)) }
      },
    )
  }
}

@DarkPreview
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
