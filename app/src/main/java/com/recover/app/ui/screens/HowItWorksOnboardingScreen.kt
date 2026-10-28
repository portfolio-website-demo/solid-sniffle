package com.recover.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recover.app.R
import com.recover.app.ui.components.WhiteFocusCard
import com.recover.app.ui.theme.RecoverMagentaDot
import kotlinx.coroutines.launch

/**
 * Screens 3, 4, 5, 6 — Matches `assets/onboarding/3.png`, `4.png`, `5.png`, `6.png`
 * Uses confirmed assets from `assets/onboarding/onboarding assets/`:
 * - `onboarding_sky_bg.png`
 * - `onboarding_phone_1.png`
 * - `onboarding_phone_2.png`
 * - `onboarding_phone_3.png`
 */
@Composable
fun HowItWorksOnboardingScreen(
    creditsBalance: Int,
    cardHolderName: String,
    onContinueToAuth: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // Sky background artwork (`onboarding_sky_bg.png`)
        Image(
            painter = painterResource(id = R.drawable.onboarding_sky_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Top "HOW IT WORKS" pill badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFF2F4F7))
                    .padding(horizontal = 16.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "HOW IT WORKS",
                    color = Color(0xFF474B52),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.4.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Headline per slide
            if (pagerState.currentPage < 3) {
                Text(
                    text = "How Recover Helps You Overcome\nSocial-Media Addiction",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 30.sp
                )
            } else {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.White)) {
                            append("Your ")
                        }
                        withStyle(SpanStyle(color = Color(0xFFC2E9FF))) {
                            append("Focus Card")
                        }
                        withStyle(SpanStyle(color = Color.White)) {
                            append(" is how you\npay for screen time.")
                        }
                    },
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
            }

            // Swipeable HorizontalPager for Slides 1–4 (`3.png` to `6.png`)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> PhoneSlidePage(
                        imageRes = R.drawable.onboarding_phone_1,
                        caption = "START A FOCUS SESSION AND\nEARN SCREEN TIME BY\nTHE MINUTE."
                    )

                    1 -> PhoneSlidePage(
                        imageRes = R.drawable.onboarding_phone_2,
                        caption = "PUT YOUR PHONE DOWN -\nMOTION DETECTION KEEPS\nYOU HONEST."
                    )

                    2 -> PhoneSlidePage(
                        imageRes = R.drawable.onboarding_phone_3,
                        caption = "READY TO SCROLL? WITHDRAW\nTO UNLOCK YOUR APPS -\nANYTIME."
                    )

                    3 -> FocusCardSlidePage(
                        creditsBalance = creditsBalance,
                        cardHolderName = cardHolderName
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4-pill indicator bar (cumulative magenta fill matching `3.png`..`6.png`)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    val isActive = index <= pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .width(28.dp)
                            .height(7.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (isActive) RecoverMagentaDot else Color(0xFF7B90A6).copy(alpha = 0.78f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Bottom black pill button ("Next" on slides 1–3, "Continue" on slide 4)
            Button(
                onClick = {
                    if (pagerState.currentPage < 3) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onContinueToAuth()
                    }
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
            ) {
                Text(
                    text = if (pagerState.currentPage < 3) "Next" else "Continue",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun PhoneSlidePage(
    imageRes: Int,
    caption: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = caption,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = caption,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 25.sp,
            letterSpacing = 0.3.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

/**
 * Slide 4 (`assets/onboarding/6.png`) — Focus Card hero illustration + 4 value bullets.
 * Uses the single v1 WhiteFocusCard in front of decorative fan silhouettes matching `6.png`.
 */
@Composable
private fun FocusCardSlidePage(
    creditsBalance: Int,
    cardHolderName: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Fan backdrop + foreground WhiteFocusCard matching `6.png` composition
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Decorative fan silhouettes behind the primary Focus Card
            FanSilhouetteCard(color = Color(0xFFAEEA00), rotationDeg = -28f, offsetY = (-16).dp)
            FanSilhouetteCard(color = Color(0xFFF5F7FA), rotationDeg = -19f, offsetY = (-14).dp)
            FanSilhouetteCard(color = Color(0xFF0066FF), rotationDeg = -10f, offsetY = (-10).dp)
            FanSilhouetteCard(color = Color(0xFF2B2D31), rotationDeg = -2f, offsetY = (-6).dp)
            FanSilhouetteCard(color = Color(0xFFF4A2B8), rotationDeg = 7f, offsetY = (-2).dp)
            FanSilhouetteCard(color = Color(0xFFA32CC4), rotationDeg = 15f, offsetY = 4.dp)

            // Foreground single v1 White Focus Card
            WhiteFocusCard(
                creditsBalance = creditsBalance,
                cardHolderName = cardHolderName,
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .rotate(2.5f)
            )
        }

        // 4 Bullet points matching `6.png`
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OnboardingBulletRow("Spending limits that stop you over-scrolling")
            OnboardingBulletRow("A visible cost that makes every minute count")
            OnboardingBulletRow("A balance you’ve earned — and won’t want\nto lose")
            OnboardingBulletRow("A growing record that proves you’re\nimproving")
        }
    }
}

@Composable
private fun FanSilhouetteCard(
    color: Color,
    rotationDeg: Float,
    offsetY: androidx.compose.ui.unit.Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.76f)
            .height(155.dp)
            .offset(y = offsetY)
            .rotate(rotationDeg)
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(color, color.copy(alpha = 0.85f))
                )
            )
    )
}

@Composable
private fun OnboardingBulletRow(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(Color(0xFFD6EEFF))
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp
        )
    }
}
