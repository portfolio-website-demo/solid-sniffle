package com.recover.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recover.app.R
import com.recover.app.ui.theme.RecoverElectricBlue
import kotlinx.coroutines.delay

/**
 * Screen 1 — Matches `assets/splash screen/1.png`
 * Solid bright electric blue screen with centered white Recover winged emblem.
 */
@Composable
fun SplashLogoScreen(
    onNavigateNext: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1200L)
        onNavigateNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RecoverElectricBlue)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onNavigateNext
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.recover_logo_winged_black),
            contentDescription = "Recover Winged Logo",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier.size(width = 96.dp, height = 74.dp)
        )
    }
}

/**
 * Screen 2 — Matches `assets/splash screen/2.jpg`
 * Solid bright electric blue screen with centered white circular orb and bottom black pill "Get Started ->" button.
 */
@Composable
fun SplashWelcomeScreen(
    onGetStartedClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RecoverElectricBlue)
            .navigationBarsPadding()
            .padding(horizontal = 32.dp, vertical = 28.dp)
    ) {
        // Centered white circular dot matching `2.jpg`
        Box(
            modifier = Modifier
                .size(42.dp)
                .align(Alignment.Center)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFFF3F7FF)
                        )
                    )
                )
        )

        // Bottom black pill button "Get Started ->"
        Button(
            onClick = onGetStartedClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                text = "Get Started   \u2192",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.2.sp
            )
        }
    }
}
