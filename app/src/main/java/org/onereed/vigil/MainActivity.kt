package org.onereed.vigil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import org.onereed.shared.logging.LifecycleLogger.Companion.addLogger
import org.onereed.vigil.common.VigilFrame

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  init {
    lifecycle.addLogger()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent { VigilFrame { TopLevelScreen() } }
  }
}
