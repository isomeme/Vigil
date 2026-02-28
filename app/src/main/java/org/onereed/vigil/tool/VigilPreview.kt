package org.onereed.vigil.tool

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.onereed.vigil.ui.theme.VigilTheme

@Composable
fun VigilPreview(content: @Composable BoxScope.() -> Unit) {
  VigilTheme {
    Box(
      modifier = Modifier.fillMaxSize().padding(10.dp),
      contentAlignment = Alignment.Center,
      content = content,
    )
  }
}
