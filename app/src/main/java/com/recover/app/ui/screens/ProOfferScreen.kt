package com.recover.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recover.app.R

/**
 * Screen 8 — Matches `assets/onboarding/8.png`
 * Recover Pro offer screen with working top-right "Skip ->" button,
 * black app icon badge, translucent dark membership card, and honest Play Billing status.
 */
@Composable
fun ProOfferScreen(
    onSkipToPermissions: () -> Unit
) {
    var showBillingNotConfiguredDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Cloud background (`onboarding_sky_bg.png`) with dark atmospheric slate gradient matching `8.png`
        Image(
            painter = painterResource(id = R.drawable.onboarding_sky_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2E4968).copy(alpha = 0.58f),
                            Color(0xFF141B24).copy(alpha = 0.88f),
                            Color(0xFF07090D).copy(alpha = 0.98f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar with right-aligned "Skip ->" button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Skip  \u2192",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onSkipToPermissions)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Black rounded-square app icon with white Recover winged emblem
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.recover_logo_winged_black),
                    contentDescription = "Recover Icon",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(width = 46.dp, height = 35.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Recover Pro",
                color = Color.White,
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Take control of your time.\nUnlock your full focus potential.",
                color = Color(0xFFD5DCE4),
                fontSize = 15.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Main Dark Glassmorphic Card matching `8.png`
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF1F262E).copy(alpha = 0.90f))
                    .border(1.dp, Color(0xFF3E4854), RoundedCornerShape(24.dp))
                    .padding(horizontal = 22.dp, vertical = 22.dp)
            ) {
                Text(
                    text = "RECOVER PRO",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 0.6.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "More focus. Fewer distractions. Greater control.",
                    color = Color(0xFFC2CAD4),
                    fontSize = 13.5.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dropdown Box: "Recover Pro Membership"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF2B333C))
                        .border(1.dp, Color(0xFF434D58), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recover Pro Membership",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Price Row: ₹199 /month
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "\u20B9199",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "/month",
                        color = Color(0xFFC4CCD6),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.alignByBaseline()
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // White Pill Button: "Upgrade to Recover Pro"
                Button(
                    onClick = { showBillingNotConfiguredDialog = true },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Upgrade to Recover Pro",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(color = Color(0xFF343D46), thickness = 1.dp)

                Spacer(modifier = Modifier.height(18.dp))

                // 4 Feature Checkmark Rows
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    ProFeatureItem("Ad-free experience")
                    ProFeatureItem("Customise your Recover Wallet cards")
                    ProFeatureItem("Block up to 10 distracting apps")
                    ProFeatureItem("Unlock focus analytics")
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Footer links: Terms of Service • Privacy Policy & Restore Subscription
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Terms of Service",
                    color = Color.White,
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.Underline
                )
                Text(
                    text = "   \u2022   ",
                    color = Color(0xFF9EA6B2),
                    fontSize = 13.sp
                )
                Text(
                    text = "Privacy Policy",
                    color = Color.White,
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Restore Subscription",
                color = Color(0xFFA7B0BC),
                fontSize = 13.sp,
                modifier = Modifier
                    .clickable { showBillingNotConfiguredDialog = true }
                    .padding(4.dp)
            )
        }
    }

    if (showBillingNotConfiguredDialog) {
        AlertDialog(
            onDismissRequest = { showBillingNotConfiguredDialog = false },
            containerColor = Color(0xFF181B20),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCFD6DF),
            title = { Text("Google Play Billing Not Configured", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Google Play Billing merchant credentials and product SKUs are not configured in this build.\n\n" +
                        "Per Recover engineering rules, billing and Pro entitlements are never faked. You can skip and use Recover Free."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBillingNotConfiguredDialog = false
                        onSkipToPermissions()
                    }
                ) {
                    Text("Skip to App Setup", color = Color(0xFF4D94FF), fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showBillingNotConfiguredDialog = false }) {
                    Text("Close", color = Color(0xFF9EA3AC))
                }
            }
        )
    }
}

@Composable
private fun ProFeatureItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(1.8.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = text,
            color = Color(0xFFE6EBF2),
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Normal
        )
    }
}
