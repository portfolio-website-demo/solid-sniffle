package com.recover.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recover.app.R
import com.recover.app.ui.theme.RecoverDeepRoyalBlue

/**
 * Screen 7 — Matches `assets/onboarding/7.png`
 * Deep royal blue upper area with centered `recover_wordmark.png`,
 * and bottom black rounded sheet with:
 * - "Continue with Google"
 * - "Sign up with email"
 * - "Log in"
 * - Terms of Service & Privacy Policy
 *
 * Strictly honors the no-fake-auth requirement: reports missing OAuth/cloud backend honestly
 * and lets the user save a local device profile to proceed to Screen 8 (Recover Pro offer).
 */
@Composable
fun AuthScreen(
    initialDisplayName: String,
    onCompleteAuthStep: (displayName: String, email: String?) -> Unit
) {
    var showGoogleUnconfiguredDialog by remember { mutableStateOf(false) }
    var showEmailDialogMode by remember { mutableStateOf<String?>(null) } // "Sign up" or "Log in"
    var showLegalDialogTitle by remember { mutableStateOf<String?>(null) }

    var nameInput by remember { mutableStateOf(initialDisplayName.ifBlank { "JJ" }) }
    var emailInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RecoverDeepRoyalBlue)
    ) {
        // Upper 62% Royal Blue Canvas with centered RECOVER wordmark
        Box(
            modifier = Modifier
                .weight(0.62f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.recover_wordmark),
                contentDescription = "RECOVER",
                modifier = Modifier
                    .fillMaxWidth(0.66f)
                    .height(44.dp)
            )
        }

        // Bottom 38% Rounded Black Sheet matching `7.png`
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(Color(0xFF060608))
                .navigationBarsPadding()
                .padding(horizontal = 22.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Continue with Google
            Button(
                onClick = { showGoogleUnconfiguredDialog = true },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2B2B2E),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    GoogleGLogo(modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Continue with Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 2. Sign up with email
            Button(
                onClick = { showEmailDialogMode = "Sign up with email" },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2B2B2E),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Sign up with email",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 3. Log in (outlined dark button)
            OutlinedButton(
                onClick = { showEmailDialogMode = "Log in" },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFF48484E)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(
                    text = "Log in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Terms & Privacy footer text matching `7.png`
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color(0xFFB5B8C0))) {
                        append("By signing up with Google, you agree to our\n")
                    }
                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Terms of Service")
                    }
                    withStyle(SpanStyle(color = Color(0xFFB5B8C0))) {
                        append(" and consent to our ")
                    }
                    withStyle(
                        SpanStyle(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append("Privacy Policy")
                    }
                    withStyle(SpanStyle(color = Color(0xFFB5B8C0))) {
                        append(".")
                    }
                },
                fontSize = 12.5.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable {
                    showLegalDialogTitle = "Terms of Service & Privacy Policy"
                }
            )
        }
    }

    // Honest Google OAuth Unconfigured Dialog
    if (showGoogleUnconfiguredDialog) {
        AlertDialog(
            onDismissRequest = { showGoogleUnconfiguredDialog = false },
            containerColor = Color(0xFF18181C),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCFD3DC),
            title = { Text("Google Sign-In Not Configured", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Google OAuth credentials (google-services.json / client ID) are not configured in this standalone build.\n\n" +
                        "Recover will never fake cloud authentication. You can continue using a local on-device profile."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showGoogleUnconfiguredDialog = false
                        onCompleteAuthStep(nameInput, null)
                    }
                ) {
                    Text("Continue Locally", color = Color(0xFF4D94FF), fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showGoogleUnconfiguredDialog = false }) {
                    Text("Cancel", color = Color(0xFF9EA3AC))
                }
            }
        )
    }

    // Honest Email / Local Profile Setup Dialog
    if (showEmailDialogMode != null) {
        AlertDialog(
            onDismissRequest = { showEmailDialogMode = null },
            containerColor = Color(0xFF18181C),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCFD3DC),
            title = { Text(showEmailDialogMode ?: "Account Setup", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Cloud authentication is not configured in this build. Your cardholder name and email are stored locally on your device.",
                        fontSize = 13.sp,
                        color = Color(0xFFA8AEB8)
                    )
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Cardholder Name") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4D94FF),
                            unfocusedBorderColor = Color(0xFF48484E),
                            focusedLabelColor = Color(0xFF4D94FF),
                            unfocusedLabelColor = Color(0xFF9EA3AC)
                        )
                    )
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Email (optional for local profile)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF4D94FF),
                            unfocusedBorderColor = Color(0xFF48484E),
                            focusedLabelColor = Color(0xFF4D94FF),
                            unfocusedLabelColor = Color(0xFF9EA3AC)
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val chosenName = nameInput.trim().ifEmpty { "JJ" }
                        val chosenEmail = emailInput.trim().takeIf { it.isNotEmpty() }
                        showEmailDialogMode = null
                        onCompleteAuthStep(chosenName, chosenEmail)
                    }
                ) {
                    Text("Continue", color = Color(0xFF4D94FF), fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEmailDialogMode = null }) {
                    Text("Cancel", color = Color(0xFF9EA3AC))
                }
            }
        )
    }

    if (showLegalDialogTitle != null) {
        AlertDialog(
            onDismissRequest = { showLegalDialogTitle = null },
            containerColor = Color(0xFF18181C),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCFD3DC),
            title = { Text(showLegalDialogTitle ?: "Legal", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Recover stores your focus sessions, credit ledger, and selected distracting app packages locally on your device using Android Room and DataStore. Camera frames and Accessibility window events are processed strictly on-device and are never uploaded."
                )
            },
            confirmButton = {
                TextButton(onClick = { showLegalDialogTitle = null }) {
                    Text("Close", color = Color(0xFF4D94FF))
                }
            }
        )
    }
}

@Composable
private fun GoogleGLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.22f
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
        val arcSize = Size(diameter, diameter)

        // Red top arc
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 200f,
            sweepAngle = 115f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )
        // Yellow left arc
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 135f,
            sweepAngle = 65f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )
        // Green bottom arc
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 45f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )
        // Blue right arc + crossbar
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -15f,
            sweepAngle = 60f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
        )
        drawLine(
            color = Color(0xFF4285F4),
            start = Offset(size.width * 0.52f, size.height * 0.5f),
            end = Offset(size.width * 0.94f, size.height * 0.5f),
            strokeWidth = strokeWidth
        )
    }
}
