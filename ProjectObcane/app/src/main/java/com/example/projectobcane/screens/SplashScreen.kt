package com.example.projectobcane.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projectobcane.navigation.INavigationRouter
import kotlinx.coroutines.delay
import com.example.projectobcane.R
import com.example.projectobcane.utils.OnboardingPreferences

@Composable
fun SplashScreen(navigation: INavigationRouter) {
    val rotation = remember { Animatable(0f) }

    var context = LocalContext.current

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = tween(durationMillis = 2000, easing = LinearEasing)
        )

        //if () is true send to splash screen
        //else send him to main screen

        val completed = OnboardingPreferences.isCompleted(context)

        if (completed) {
            navigation.navigateToMainScreen()
        } else {
            navigation.navigateToOnBoarding1()
        }


    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.infoobce),
            contentDescription = stringResource(R.string.app_icon),
            modifier = Modifier
                .size(128.dp)
                .graphicsLayer {
                    rotationZ = rotation.value
                }
        )
    }
}

