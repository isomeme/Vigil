package org.onereed.vigil.common

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.content.ContextCompat.checkSelfPermission

// Utility methods for checking app build and permission states.

@ChecksSdkIntAtLeast(parameter = 0)
fun sdkAtLeast(version: Int): Boolean = Build.VERSION.SDK_INT >= version

fun Context.hasPermission(permission: String): Boolean {
  val minSdk =
    permissionToMinSdk[permission]
      ?: throw IllegalArgumentException("No minSdk for permission '$permission'")

  return !sdkAtLeast(minSdk) || checkSelfPermission(this, permission) == PERMISSION_GRANTED
}

@SuppressLint("InlinedApi")
private val permissionToMinSdk: Map<String, Int> =
  mapOf(POST_NOTIFICATIONS to VERSION_CODES.TIRAMISU)
