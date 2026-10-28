package com.recover.app.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.recover.app.domain.model.PermissionCapability
import com.recover.app.platform.permissions.PermissionChecker

/**
 * Screen 9 — Matches `assets/onboarding/9.png`
 * Consolidated App Permissions setup screen:
 * 1. Accessibility Service (blue shield icon)
 * 2. Notifications (yellow bell icon)
 * 3. Display over other apps (purple overlapping squares icon)
 *
 * Strictly reflects real Android OS permission state and re-checks on ON_RESUME.
 * Never fakes a permission toggle in local UI state.
 */
@Composable
fun PermissionsSetupScreen(
    onFinishPermissions: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var permissionStates by remember {
        mutableStateOf(PermissionChecker.checkAllPermissions(context))
    }
    var showAccessibilityDisclosureDialog by remember { mutableStateOf(false) }
    var showWhyNeededDialog by remember { mutableStateOf(false) }

    // Recheck real OS permission state every time user returns from Android Settings
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                permissionStates = PermissionChecker.checkAllPermissions(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        permissionStates = PermissionChecker.checkAllPermissions(context)
    }

    val isAccessibilityGranted =
        permissionStates[PermissionCapability.ACCESSIBILITY_ENFORCEMENT]?.isGranted == true
    val isNotificationsGranted =
        permissionStates[PermissionCapability.NOTIFICATIONS]?.isGranted == true
    val isOverlayGranted =
        permissionStates[PermissionCapability.OVERLAY_DISPLAY]?.isGranted == true

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 22.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "App Permissions",
                color = Color.White,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "One last thing! Enable the following permissions\nto help Recover manage your focus sessions\nand block distractions.",
                color = Color(0xFFD0D4DC),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Card 1: Accessibility Service
            PermissionCard(
                title = "Accessibility Service",
                description = "Allow Recover to detect and restrict selected distracting apps during focus sessions.",
                isGranted = isAccessibilityGranted,
                iconContent = { BlueShieldIcon() },
                onClickToggle = {
                    if (isAccessibilityGranted) {
                        runCatching {
                            context.startActivity(PermissionChecker.createAccessibilitySettingsIntent())
                        }
                    } else {
                        showAccessibilityDisclosureDialog = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card 2: Notifications
            PermissionCard(
                title = "Notifications",
                description = "Receive focus session updates, reminders, and completion alerts.",
                isGranted = isNotificationsGranted,
                iconContent = { YellowBellIcon() },
                onClickToggle = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isNotificationsGranted) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        runCatching {
                            context.startActivity(PermissionChecker.createNotificationSettingsIntent(context))
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card 3: Display over other apps
            PermissionCard(
                title = "Display over other apps",
                description = "Allow Recover to show overlays like blocks, timers, and important alerts on top of other apps.",
                isGranted = isOverlayGranted,
                iconContent = { PurpleOverlaySquaresIcon() },
                onClickToggle = {
                    runCatching {
                        context.startActivity(PermissionChecker.createOverlayPermissionIntent(context))
                    }
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Footer disclosure link matching `9.png`
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color(0xFFB3B7C0))) {
                        append("You can review ")
                    }
                    withStyle(
                        SpanStyle(
                            color = Color(0xFF2979FF),
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("why these permissions are needed")
                    }
                    withStyle(SpanStyle(color = Color(0xFFB3B7C0))) {
                        append("\nand manage them later in your phone’s Settings.")
                    }
                },
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable { showWhyNeededDialog = true }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Bottom White Pill Button: "Finish"
        Button(
            onClick = onFinishPermissions,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Finish",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // Prominent Google Play Accessibility Service Disclosure Dialog
    if (showAccessibilityDisclosureDialog) {
        AlertDialog(
            onDismissRequest = { showAccessibilityDisclosureDialog = false },
            containerColor = Color(0xFF18181C),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFD0D4DC),
            title = { Text("Accessibility Service Disclosure", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Recover uses the Android AccessibilityService API solely to detect the foreground application package name when you open an app.\n\n" +
                        "• If the opened package is in your selected Distracting Apps list during an active focus session (and does not have an active 60-second credit unlock), Recover blocks it.\n" +
                        "• Recover never reads, stores, or transmits your screen text, passwords, messages, or personal data."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showAccessibilityDisclosureDialog = false
                        runCatching {
                            context.startActivity(PermissionChecker.createAccessibilitySettingsIntent())
                        }
                    }
                ) {
                    Text("Open Settings", color = Color(0xFF4D94FF), fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAccessibilityDisclosureDialog = false }) {
                    Text("Not Now", color = Color(0xFF9EA3AC))
                }
            }
        )
    }

    if (showWhyNeededDialog) {
        AlertDialog(
            onDismissRequest = { showWhyNeededDialog = false },
            containerColor = Color(0xFF18181C),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFD0D4DC),
            title = { Text("Why Recover Needs These Permissions", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "1. Accessibility Service: Required for real OS enforcement so selected distracting apps are genuinely blocked when opened.\n\n" +
                        "2. Notifications: Keeps your focus session timer and 60-second unlock countdown visible.\n\n" +
                        "3. Display over other apps: Displays the block/unlock prompt and countdown overlay when a restricted app is opened.\n\n" +
                        "Note: Camera permission is requested contextually only when you start the AR focus zone setup."
                )
            },
            confirmButton = {
                TextButton(onClick = { showWhyNeededDialog = false }) {
                    Text("Got It", color = Color(0xFF4D94FF), fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}

@Composable
private fun PermissionCard(
    title: String,
    description: String,
    isGranted: Boolean,
    iconContent: @Composable () -> Unit,
    onClickToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF151517))
            .border(1.dp, Color(0xFF232328), RoundedCornerShape(20.dp))
            .clickable(onClick = onClickToggle)
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Top),
            contentAlignment = Alignment.Center
        ) {
            iconContent()
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                color = Color(0xFF9EA3AC),
                fontSize = 13.5.sp,
                lineHeight = 19.sp
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        OsStateSwitch(
            checked = isGranted,
            onClick = onClickToggle
        )
    }
}

@Composable
private fun OsStateSwitch(
    checked: Boolean,
    onClick: () -> Unit
) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF2979FF) else Color(0xFF35353A),
        label = "switchTrack"
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 4.dp,
        label = "switchThumb"
    )

    Box(
        modifier = Modifier
            .width(54.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(50))
            .background(trackColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

@Composable
private fun BlueShieldIcon() {
    Canvas(modifier = Modifier.size(30.dp)) {
        val w = size.width
        val h = size.height
        val shieldPath = Path().apply {
            moveTo(w * 0.5f, h * 0.06f)
            lineTo(w * 0.88f, h * 0.20f)
            lineTo(w * 0.88f, h * 0.50f)
            cubicTo(w * 0.88f, h * 0.75f, w * 0.5f, h * 0.94f, w * 0.5f, h * 0.94f)
            cubicTo(w * 0.5f, h * 0.94f, w * 0.12f, h * 0.75f, w * 0.12f, h * 0.50f)
            lineTo(w * 0.12f, h * 0.20f)
            close()
        }
        drawPath(shieldPath, color = Color(0xFF2979FF))
        // Right half darker shade matching `9.png`
        val rightHalf = Path().apply {
            moveTo(w * 0.5f, h * 0.12f)
            lineTo(w * 0.82f, h * 0.24f)
            lineTo(w * 0.82f, h * 0.49f)
            cubicTo(w * 0.82f, h * 0.70f, w * 0.5f, h * 0.87f, w * 0.5f, h * 0.87f)
            close()
        }
        drawPath(rightHalf, color = Color(0xFF0E1B33))
    }
}

@Composable
private fun YellowBellIcon() {
    Canvas(modifier = Modifier.size(30.dp)) {
        val w = size.width
        val h = size.height
        val bellColor = Color(0xFFF6C23E)
        val bellPath = Path().apply {
            moveTo(w * 0.5f, h * 0.10f)
            cubicTo(w * 0.30f, h * 0.10f, w * 0.22f, h * 0.28f, w * 0.22f, h * 0.52f)
            lineTo(w * 0.12f, h * 0.72f)
            lineTo(w * 0.88f, h * 0.72f)
            lineTo(w * 0.78f, h * 0.52f)
            cubicTo(w * 0.78f, h * 0.28f, w * 0.70f, h * 0.10f, w * 0.5f, h * 0.10f)
            close()
        }
        drawPath(bellPath, color = bellColor)
        drawCircle(
            color = bellColor,
            radius = w * 0.10f,
            center = Offset(w * 0.5f, h * 0.84f)
        )
    }
}

@Composable
private fun PurpleOverlaySquaresIcon() {
    Canvas(modifier = Modifier.size(30.dp)) {
        val w = size.width
        val h = size.height
        val purple = Color(0xFF9C6ADE)
        val stroke = Stroke(width = 2.6.dp.toPx())

        // Back top-left rounded square
        drawRoundRect(
            color = purple,
            topLeft = Offset(w * 0.10f, h * 0.10f),
            size = Size(w * 0.54f, h * 0.54f),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
            style = stroke
        )
        // Front bottom-right rounded square
        drawRoundRect(
            color = Color(0xFF151517),
            topLeft = Offset(w * 0.34f, h * 0.34f),
            size = Size(w * 0.56f, h * 0.56f),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )
        drawRoundRect(
            color = purple,
            topLeft = Offset(w * 0.34f, h * 0.34f),
            size = Size(w * 0.56f, h * 0.56f),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
            style = stroke
        )
    }
}
