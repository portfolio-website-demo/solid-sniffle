package com.recover.app.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.recover.app.domain.model.BlockedApp
import com.recover.app.domain.model.UserSettings
import com.recover.app.ui.components.WhiteFocusCard
import com.recover.app.ui.theme.RecoverElectricBlue

/**
 * Dashboard Shell Screen (`assets/dashboard/dashboard1.png`) reached after Screen 10 ("Distracting Apps"),
 * showing the user's single v1 White Focus Card with real ledger balance, persisted selected distracting apps,
 * and quick navigation to edit distracting apps or replay onboarding.
 */
@Composable
fun DashboardShellScreen(
    userSettings: UserSettings,
    creditsBalance: Int,
    selectedBlockedApps: List<BlockedApp>,
    onEditBlockedApps: () -> Unit,
    onOpenProOffer: () -> Unit,
    onOpenPermissions: () -> Unit,
    onReplayOnboarding: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            // Top App Bar: Settings Gear + "recover" + "Pro" pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1A1A1E))
                        .clickable(onClick = onOpenPermissions),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Permissions & Settings",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "recover",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF1A1A1E))
                        .border(1.dp, Color(0xFF2C2C32), RoundedCornerShape(50))
                        .clickable(onClick = onOpenProOffer)
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = "Pro",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Single v1 White Focus Card bound to real Room ledger balance
            WhiteFocusCard(
                creditsBalance = creditsBalance,
                cardHolderName = userSettings.displayName,
                cardNumber = userSettings.cardNumber
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Quick Actions Row: Transactions / Withdraw / Analytics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DashboardActionCircle(
                    icon = Icons.Default.History,
                    label = "Transactions",
                    onClick = {}
                )
                DashboardActionCircle(
                    icon = Icons.Default.ArrowDownward,
                    label = "Withdraw",
                    onClick = onEditBlockedApps
                )
                DashboardActionCircle(
                    icon = Icons.Default.BarChart,
                    label = "Analytics",
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Bento Grid Row: Choose distracting apps + Focus Streak
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Choose distracting apps widget bound to real persisted selections
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(175.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF141417))
                        .border(1.dp, Color(0xFF222228), RoundedCornerShape(22.dp))
                        .clickable(onClick = onEditBlockedApps)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Choose distracting ...",
                                color = Color(0xFF9EA3AC),
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color(0xFF9EA3AC),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "${selectedBlockedApps.size} apps",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (selectedBlockedApps.isEmpty()) {
                                "Tap to select installed apps"
                            } else {
                                selectedBlockedApps.joinToString(", ") { it.label }
                            },
                            color = Color(0xFF8C919B),
                            fontSize = 13.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Text(
                        text = "Screen Time",
                        color = Color(0xFF7B808A),
                        fontSize = 13.sp
                    )
                }

                // Recover Pro / Onboarding preview widget
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(175.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF141923))
                        .border(1.dp, Color(0xFF263142), RoundedCornerShape(22.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Recover Setup",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Splash 1–2 ready\n• How It Works 3–6\n• Auth & Pro 7–8\n• Perms & Apps 9–10",
                            color = Color(0xFFCFD6E0),
                            fontSize = 11.5.sp,
                            lineHeight = 16.sp
                        )
                    }

                    Button(
                        onClick = onReplayOnboarding,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RecoverElectricBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                    ) {
                        Text(
                            text = "Review 1–10",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Primary bottom-of-dashboard white CTA card ("Add funds / Start a focus session to earn")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Add funds",
                        color = Color.Black,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Start a focus session to earn",
                        color = Color(0xFF42454A),
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Bottom Navigation Bar matching `dashboard1.png`
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF141417))
                .border(1.dp, Color(0xFF232329), RoundedCornerShape(50))
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BottomNavPill(
                icon = Icons.Default.CreditCard,
                label = "Card",
                selected = true,
                modifier = Modifier.weight(1f)
            )
            BottomNavPill(
                icon = Icons.Default.Leaderboard,
                label = "Rankings",
                selected = false,
                modifier = Modifier.weight(1f)
            )
            BottomNavPill(
                icon = Icons.Default.Person,
                label = "Account",
                selected = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DashboardActionCircle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Color(0xFF1A1A1F)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BottomNavPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) Color(0xFF0A0A0D) else Color.Transparent)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
