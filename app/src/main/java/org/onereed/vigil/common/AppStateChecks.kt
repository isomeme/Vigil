package org.onereed.vigil.common

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.core.content.ContextCompat

// Utility methods for checking app build and permission states.

@ChecksSdkIntAtLeast(parameter = 0)
fun sdkAtLeast(version: Int): Boolean = Build.VERSION.SDK_INT >= version

fun Context.hasPermission(permission: String): Boolean =
  ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
