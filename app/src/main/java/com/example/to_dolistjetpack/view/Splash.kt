package com.example.to_dolistjetpack.view

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.to_dolistjetpack.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var isAnimationStarted by remember { mutableStateOf(false) }

    val height by animateDpAsState(
        targetValue = if (isAnimationStarted) 378.dp else 120.dp,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing), label = ""
    )

    val paddingBottom by animateDpAsState(
        targetValue = if (isAnimationStarted) 302.dp else 0.dp,
        animationSpec = tween(durationMillis = 1600, easing = FastOutSlowInEasing), label = ""
    )

    LaunchedEffect(Unit) {
        delay(1000L)
        isAnimationStarted = true
        delay(1350L)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.list),
                contentDescription = "List Icon",
                modifier = Modifier
                    .height(height)
                    .padding(bottom = paddingBottom)
            )
        }
    }
}
