package org.onereed.vigil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import org.onereed.vigil.ui.theme.VigilTheme
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Timber.d("onCreate")

    enableEdgeToEdge()
    setContent { VigilTheme { TopLevelScreen() } }
  }
}
