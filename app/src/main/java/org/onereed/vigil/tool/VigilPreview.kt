package org.onereed.vigil.tool

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import org.onereed.shared.screen.BaseScreen
import org.onereed.vigil.ui.theme.VigilTheme

@Composable
fun VigilPreview(content: @Composable BoxScope.() -> Unit) {
  VigilTheme { BaseScreen(content) }
}
