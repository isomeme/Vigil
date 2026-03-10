package org.onereed.vigil.tool

import android.content.res.Configuration
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.onereed.vigil.common.BaseScreen
import org.onereed.vigil.ui.theme.VigilTheme

@Composable
fun VigilPreview(content: @Composable BoxScope.() -> Unit) {
  VigilTheme { BaseScreen(content) }
}

@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class DarkPreview

@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
annotation class LightPreview

@DarkPreview @LightPreview annotation class ThemePreviews
