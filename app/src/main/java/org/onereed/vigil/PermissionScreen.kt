package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
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
import org.onereed.vigil.common.openSettings
import org.onereed.vigil.tool.DarkPreview
import org.onereed.vigil.tool.VigilPreview
import timber.log.Timber

@SuppressLint("InlinedApi")
@Composable
fun PermissionScreen(onPermissionGranted: () -> Unit) {
  Timber.d("PermissionScreen start")

  val activity = LocalActivity.current!!

  // We increment requestCount to trigger recomposition after each time a launched permission
  // request reports that the user denied permission. This provides the "heartbeat" that triggers
  // requestMode state machine changes. Note that sending the user to the system settings to
  // manually grant permission does not result in an increment of requestCount, so no further
  // recompositions occur once we reach that state.

  var requestCount by rememberSaveable { mutableIntStateOf(0) }
  val requestMode by remember {
    derivedStateOf {
      when {
        activity.shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) ->
          RequestMode.SHOW_RATIONALE
        requestCount > 0 -> RequestMode.SEND_TO_SETTINGS
        else -> RequestMode.ASK_DIRECTLY
      }
    }
  }

  LaunchedEffect(key1 = requestMode) { Timber.d("Δ requestMode -> %s", requestMode) }

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

  when (requestMode) {
    RequestMode.ASK_DIRECTLY ->
      // The user has not previously been asked to grant the permission. Show them the normal system
      // permission dialog over an otherwise blank screen. The launch callback is guaranteed to
      // either (1) trigger a TopLevelScreen recomposition that makes this screen irrelevant, or
      // (2) update requestCount in a way that makes this state unreachable.

      LaunchedEffect(Unit) { permissionRequestLauncher.launch(POST_NOTIFICATIONS) }

    RequestMode.SHOW_RATIONALE ->
      // The user has denied the permission previously, but Android will still show them the normal
      // system permission dialog. Explain why we need the permission, and provide an opportunity to
      // grant it.

      StatelessPermissionScreen(
        explanationRes = R.string.notification_permission_rationale,
        onOk = { permissionRequestLauncher.launch(POST_NOTIFICATIONS) },
        onExit = activity::finish,
      )

    RequestMode.SEND_TO_SETTINGS ->
      // Android will no longer show the user the normal system permission dialog. Explain that they
      // can grant the permission in the app's settings, and provide an opportunity to go there.

      StatelessPermissionScreen(
        explanationRes = R.string.notification_permission_use_settings,
        onOk = activity::openSettings,
        onExit = activity::finish,
      )
  }
}

@Composable
fun StatelessPermissionScreen(
  @StringRes explanationRes: Int,
  onOk: () -> Unit,
  onExit: () -> Unit,
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
        TextButton(onClick = onOk) { Text(text = stringResource(R.string.button_ok)) }
      },
      dismissButton = {
        TextButton(onClick = onExit) { Text(text = stringResource(R.string.button_exit)) }
      },
    )
  }
}

private enum class RequestMode {
  ASK_DIRECTLY,
  SHOW_RATIONALE,
  SEND_TO_SETTINGS,
}

@DarkPreview
@Composable
fun PermissionScreenPreview() {
  VigilPreview {
    StatelessPermissionScreen(
      explanationRes = R.string.notification_permission_rationale,
      onOk = {},
      onExit = {},
    )
  }
}
