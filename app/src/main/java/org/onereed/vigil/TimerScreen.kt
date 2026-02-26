package org.onereed.vigil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.onereed.vigil.ui.theme.VigilTheme

@Composable
fun TimerScreen(viewModel: TimerViewModel = hiltViewModel()) {
  val seconds by viewModel.timerProgress.collectAsStateWithLifecycle(initialValue = 0)

  StatelessTimerScreen(seconds, viewModel::startTimer, viewModel::stopTimer)
}

@Composable
fun StatelessTimerScreen(seconds: Int?, onStartTimer: () -> Unit, onStopTimer: () -> Unit) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(
      verticalArrangement = Arrangement.spacedBy(20.dp),
      horizontalAlignment = Alignment.Start,
    ) {
      Text("Timer: $seconds")
      Button(onClick = onStartTimer) { Text("Start") }
      Button(onClick = onStopTimer) { Text("Stop") }
    }
  }
}

@Preview
@Composable
fun TimerScreenPreview() {
  VigilTheme { StatelessTimerScreen(42, {}, {}) }
}
