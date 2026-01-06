package com.ihita.wholetthemcook.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimateImage(
    painter: Painter,
    isLeft: Boolean,
    imageSize: Dp,
    spreadX: Float,
    riseY: Float
) {

    val direction = if (isLeft) -1f else 1f

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.85f) }
    val rotation = remember {
        Animatable(if (isLeft) -12f else 12f)
    }

    LaunchedEffect(Unit) {

        delay(120)

        launch {
            offsetX.animateTo(
                targetValue = spreadX * direction,
                animationSpec = tween(
                    durationMillis = 900,
                    easing = FastOutSlowInEasing
                )
            )
        }

        launch {
            offsetY.animateTo(
                targetValue = -riseY,
                animationSpec = tween(
                    durationMillis = 900,
                    easing = FastOutSlowInEasing
                )
            )
        }

        launch {
            alpha.animateTo(
                1f,
                animationSpec = tween(700)
            )
        }

        launch {
            scale.animateTo(
                1.05f,
                animationSpec = tween(
                    durationMillis = 900,
                    easing = FastOutSlowInEasing
                )
            )
        }

        rotation.animateTo(
            targetValue = -rotation.value,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2600,
                    easing = EaseInOutSine
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .graphicsLayer {
                translationX = offsetX.value
                translationY = offsetY.value
                rotationZ = rotation.value
                scaleX = scale.value
                scaleY = scale.value
            }
            .alpha(alpha.value)
            .size(imageSize)
    )
}
