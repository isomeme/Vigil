package org.onereed.vigil

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
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
import timber.log.Timber

@Composable
fun TopLevelScreen() {
  Timber.d("TopLevelScreen start")

  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current

  fun checkPerm(): Boolean {
    @SuppressLint("InlinedApi") // Permission check always safe.
    val hasPerm = context.hasPermission(POST_NOTIFICATIONS)
    Timber.d("checkPerm = %b", hasPerm)
    return hasPerm
  }

  var hasPerm by rememberSaveable {
    Timber.d("remember")
    mutableStateOf(checkPerm())
  }

  Timber.d("top hasPerm = %b", hasPerm)

  fun updatePerm() {
    Timber.d("updatePerm")
    hasPerm = checkPerm()
  }

  fun grantPerm() {
    Timber.d("grantPerm")
    hasPerm = true
  }

  // When the user navigates away from the app then returns, the observer managed by this
  // DisposableEffect updates the permission state.

  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == ON_RESUME) {
        Timber.d("lifecycle")
        updatePerm()
      }
    }
    lifecycleOwner.lifecycle.addObserver(observer)
    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
  }

  if (hasPerm) {
    TimerScreen()
  } else {
    PermissionScreen(onPermissionGranted = ::grantPerm)
  }
}
