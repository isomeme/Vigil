package org.onereed.vigil.common

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION_CODES.TIRAMISU
import androidx.core.content.ContextCompat.checkSelfPermission

// Utility methods for checking permission states.

fun Context.hasPostNotificationsPermission(): Boolean =
  !sdkAtLeast(TIRAMISU) || hasUserPermission(POST_NOTIFICATIONS)

@Suppress("SameParameterValue")
private fun Context.hasUserPermission(permission: String): Boolean =
  checkSelfPermission(this, permission) == PERMISSION_GRANTED
