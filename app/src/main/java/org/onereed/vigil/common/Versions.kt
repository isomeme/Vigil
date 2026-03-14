package org.onereed.vigil.common

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

// Utility methods for checking app build SDK version.

@ChecksSdkIntAtLeast(parameter = 0)
fun sdkAtLeast(version: Int): Boolean = Build.VERSION.SDK_INT >= version
