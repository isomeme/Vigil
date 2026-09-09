package org.onereed.vigil.common

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import org.onereed.shared.ui.BasicFrame
import org.onereed.vigil.ui.theme.VigilTheme

@Composable
fun VigilFrame(content: @Composable BoxScope.() -> Unit) {
  VigilTheme { BasicFrame { content() } }
}
