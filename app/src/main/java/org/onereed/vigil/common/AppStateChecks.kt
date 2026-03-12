package org.onereed.vigil.common

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.content.ContextCompat.checkSelfPermission

// Utility methods for checking app build and permission states.

@ChecksSdkIntAtLeast(parameter = 0)
fun sdkAtLeast(version: Int): Boolean = Build.VERSION.SDK_INT >= version

fun Context.hasUserPermission(permission: String): Boolean =
  checkSelfPermission(this, permission) == PERMISSION_GRANTED

fun Context.hasPostNotificationsPermission(): Boolean =
  !sdkAtLeast(VERSION_CODES.TIRAMISU) || hasUserPermission(POST_NOTIFICATIONS)
