package com.recover.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recover.app.domain.model.AppCategory
import com.recover.app.domain.model.BlockedApp
import com.recover.app.platform.apps.InstalledAppDiscovery
import com.recover.app.platform.apps.InstalledAppInfo
import com.recover.app.ui.theme.RecoverElectricBlue

/**
 * Screen 10 — Matches `assets/onboarding/10.png`
 * Distracting Apps selector populated dynamically from real installed apps on the device
 * via `InstalledAppDiscovery.queryEligibleInstalledApps(context)`.
 * Never hardcodes YouTube, Instagram, TikTok, or FaceTime.
 * Persists selections by packageName and allows editing later.
 */
@Composable
fun DistractingAppsScreen(
    persistedSelections: Set<String>,
    onSaveSelections: (List<BlockedApp>) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var installedApps by remember { mutableStateOf<List<InstalledAppInfo>>(emptyList()) }
    var selectedPackages by remember(persistedSelections) {
        mutableStateOf(persistedSelections)
    }

    LaunchedEffect(Unit) {
        isLoading = true
        val discovered = InstalledAppDiscovery.queryEligibleInstalledApps(context)
        installedApps = discovered
        // Keep only persisted selections or empty if none selected yet
        if (selectedPackages.isEmpty() && persistedSelections.isNotEmpty()) {
            selectedPackages = persistedSelections
        }
        isLoading = false
    }

    val allSelected = installedApps.isNotEmpty() &&
        installedApps.all { it.packageName in selectedPackages }

    val socialApps = installedApps.filter { it.category == AppCategory.SOCIAL }
    val otherApps = installedApps.filter { it.category != AppCategory.SOCIAL }
    val primaryCategoryApps = if (socialApps.isNotEmpty()) socialApps else installedApps
    val primaryCategorySelectedCount = primaryCategoryApps.count { it.packageName in selectedPackages }
    val isPrimaryCategoryAllSelected = primaryCategoryApps.isNotEmpty() &&
        primaryCategoryApps.all { it.packageName in selectedPackages }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Header Bar matching `10.png`: 9-dot grid icon + "Distracting Apps" + circular "×" close button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                NineDotsGridIcon()
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Distracting Apps",
                    color = Color.White,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1F1F24))
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color(0xFFD6DAE2),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Main App List Container matching `10.png`
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF121215))
                .border(1.dp, Color(0xFF1E1E24), RoundedCornerShape(22.dp))
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = RecoverElectricBlue)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Scanning installed apps on device...",
                            color = Color(0xFF9EA3AC),
                            fontSize = 14.sp
                        )
                    }
                }
            } else if (installedApps.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No eligible launchable apps were found on this device.",
                        color = Color(0xFF9EA3AC),
                        fontSize = 15.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // Row 1: "All Apps & Categories"
                    item(key = "header_all_apps") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPackages = if (allSelected) {
                                        emptySet()
                                    } else {
                                        installedApps.map { it.packageName }.toSet()
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularCheckbox(checked = allSelected)
                            Spacer(modifier = Modifier.width(14.dp))
                            StackedLayersBadge()
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = "All Apps & Categories",
                                color = Color.White,
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        HorizontalDivider(color = Color(0xFF1D1D22), thickness = 1.dp)
                    }

                    // Row 2: "Social" Category Group Header matching `10.png`
                    item(key = "header_social_category") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val targetSet = primaryCategoryApps.map { it.packageName }.toSet()
                                    selectedPackages = if (isPrimaryCategoryAllSelected) {
                                        selectedPackages - targetSet
                                    } else {
                                        selectedPackages + targetSet
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularCheckbox(checked = primaryCategorySelectedCount > 0)
                            Spacer(modifier = Modifier.width(14.dp))
                            SocialCategoryBadge()
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = if (socialApps.isNotEmpty()) "Social" else "Installed Apps",
                                color = Color.White,
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )

                            // Right count pill ("3 v" matching `10.png`)
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0xFF232329))
                                    .padding(horizontal = 12.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = primaryCategorySelectedCount.toString(),
                                    color = Color(0xFFD0D4DC),
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = Color(0xFFA0A5B0),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        HorizontalDivider(color = Color(0xFF1D1D22), thickness = 1.dp)
                    }

                    // Installed apps rows
                    items(
                        items = if (socialApps.isNotEmpty()) socialApps + otherApps else installedApps,
                        key = { it.packageName }
                    ) { app ->
                        val isSelected = app.packageName in selectedPackages
                        InstalledAppRow(
                            app = app,
                            isSelected = isSelected,
                            onToggle = {
                                selectedPackages = if (isSelected) {
                                    selectedPackages - app.packageName
                                } else {
                                    selectedPackages + app.packageName
                                }
                            }
                        )
                        HorizontalDivider(
                            color = Color(0xFF1B1B20),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(start = 54.dp)
                        )
                    }
                }
            }
        }

        // Bottom Sticky Button: "Continue (X selected)" matching `10.png`
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Button(
                onClick = {
                    val now = System.currentTimeMillis()
                    val models = installedApps.map { info ->
                        BlockedApp(
                            packageName = info.packageName,
                            label = info.label,
                            category = info.category,
                            selected = info.packageName in selectedPackages,
                            updatedAtEpochMs = now
                        )
                    }
                    onSaveSelections(models)
                },
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
                    text = "Continue (${selectedPackages.size} selected)",
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun InstalledAppRow(
    app: InstalledAppInfo,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSelected) Color(0xFF1A1B20) else Color.Transparent)
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularCheckbox(checked = isSelected)

        Spacer(modifier = Modifier.width(14.dp))

        if (app.iconBitmap != null) {
            Image(
                bitmap = app.iconBitmap,
                contentDescription = app.label,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF282A32)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = app.label.take(1).uppercase(),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = app.label,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CircularCheckbox(checked: Boolean) {
    if (checked) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(RecoverElectricBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(15.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(1.6.dp, Color(0xFF43454E), CircleShape)
        )
    }
}

@Composable
private fun NineDotsGridIcon() {
    Canvas(modifier = Modifier.size(22.dp)) {
        val spacing = size.width / 3f
        val radius = 2.2.dp.toPx()
        for (row in 0..2) {
            for (col in 0..2) {
                drawCircle(
                    color = Color.White,
                    radius = radius,
                    center = Offset(
                        x = spacing * col + spacing / 2f,
                        y = spacing * row + spacing / 2f
                    )
                )
            }
        }
    }
}

@Composable
private fun StackedLayersBadge() {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF222328)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(22.dp)) {
            val w = size.width
            val h = size.height

            fun layerPath(centerY: Float): Path = Path().apply {
                moveTo(w * 0.5f, centerY - h * 0.18f)
                lineTo(w * 0.90f, centerY)
                lineTo(w * 0.5f, centerY + h * 0.18f)
                lineTo(w * 0.10f, centerY)
                close()
            }

            drawPath(layerPath(h * 0.72f), color = Color(0xFFFF6D00))
            drawPath(layerPath(h * 0.52f), color = Color(0xFFEC407A))
            drawPath(layerPath(h * 0.32f), color = Color(0xFF29B6F6))
        }
    }
}

@Composable
private fun SocialCategoryBadge() {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF222328)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(20.dp)) {
            drawCircle(
                color = Color(0xFFE91E63),
                radius = size.minDimension * 0.44f
            )
            drawCircle(
                color = Color.White,
                radius = size.minDimension * 0.16f
            )
        }
    }
}
