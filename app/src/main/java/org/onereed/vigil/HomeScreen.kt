package org.onereed.vigil

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kotlin.time.Duration.Companion.seconds
import org.onereed.vigil.ui.theme.VigilTheme

@Composable
fun HomeScreen() {
  val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")

  val animatedAlpha by
    infiniteTransition.animateFloat(
      initialValue = 0.1f,
      targetValue = 0.4f,
      animationSpec =
        infiniteRepeatable(
          animation = tween(durationMillis = PULSE_MILLIS, easing = LinearEasing),
          repeatMode = RepeatMode.Reverse,
        ),
      label = "alpha animation",
    )

  StatelessHomeScreen(animatedAlpha = animatedAlpha)
}

@Composable
fun StatelessHomeScreen(animatedAlpha: Float) {
  Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Image(
      modifier = Modifier.fillMaxSize().graphicsLayer { alpha = animatedAlpha },
      painter = painterResource(id = R.drawable.fog_circle),
      contentDescription = "Main button",
      contentScale = ContentScale.Fit,
      colorFilter =
        ColorFilter.tint(color = MaterialTheme.colorScheme.primary, blendMode = BlendMode.SrcIn),
    )
  }
}

private val PULSE = 5.seconds
private val PULSE_MILLIS = PULSE.inWholeMilliseconds.toInt()

@Preview
@Composable
fun HomeScreenPreview() {
  VigilTheme { HomeScreen() }
}
