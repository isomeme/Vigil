package org.onereed.vigil

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TimerScreen(modifier: Modifier = Modifier, viewModel: TimerViewModel = hiltViewModel()) {
    val seconds by viewModel.timerProgress.collectAsStateWithLifecycle(initialValue = 0)

    Column(modifier = modifier) {
        Text("Timer: $seconds")
        Button(onClick = { viewModel.startTimer() }) {
            Text("Start Foreground Timer")
        }
        Button(onClick = { viewModel.stopTimer() }) {
            Text("Stop")
        }
    }
}
