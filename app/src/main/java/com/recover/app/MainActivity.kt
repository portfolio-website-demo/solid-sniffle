package com.recover.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.recover.app.domain.model.UserSettings
import com.recover.app.ui.screens.AuthScreen
import com.recover.app.ui.screens.DashboardShellScreen
import com.recover.app.ui.screens.DistractingAppsScreen
import com.recover.app.ui.screens.HowItWorksOnboardingScreen
import com.recover.app.ui.screens.PermissionsSetupScreen
import com.recover.app.ui.screens.ProOfferScreen
import com.recover.app.ui.screens.SplashLogoScreen
import com.recover.app.ui.screens.SplashWelcomeScreen
import com.recover.app.ui.theme.RecoverTheme
import kotlinx.coroutines.launch

object RecoverRoutes {
    const val SPLASH_1_LOGO = "splash_1_logo"
    const val SPLASH_2_WELCOME = "splash_2_welcome"
    const val ONBOARDING_3_TO_6_HOW_IT_WORKS = "onboarding_3_to_6_how_it_works"
    const val ONBOARDING_7_AUTH = "onboarding_7_auth"
    const val ONBOARDING_8_PRO_OFFER = "onboarding_8_pro_offer"
    const val ONBOARDING_9_PERMISSIONS = "onboarding_9_permissions"
    const val ONBOARDING_10_APP_SELECTOR = "onboarding_10_app_selector"
    const val DASHBOARD_HOME = "dashboard_home"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        val app = application as RecoverApplication

        setContent {
            RecoverTheme {
                RecoverAppNavigation(app = app)
            }
        }
    }
}

@Composable
private fun RecoverAppNavigation(app: RecoverApplication) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val userSettings by app.preferencesRepository.userSettingsFlow.collectAsState(
        initial = UserSettings()
    )
    val creditsBalance by app.repository.balanceFlow.collectAsState(initial = 0)
    val selectedBlockedApps by app.repository.selectedBlockedAppsFlow.collectAsState(
        initial = emptyList()
    )
    val selectedPackageSet = selectedBlockedApps.map { it.packageName }.toSet()

    NavHost(
        navController = navController,
        startDestination = RecoverRoutes.SPLASH_1_LOGO
    ) {
        // 1.png — Splash Logo Screen
        composable(RecoverRoutes.SPLASH_1_LOGO) {
            SplashLogoScreen(
                onNavigateNext = {
                    if (userSettings.hasCompletedOnboarding) {
                        navController.navigate(RecoverRoutes.DASHBOARD_HOME) {
                            popUpTo(RecoverRoutes.SPLASH_1_LOGO) { inclusive = true }
                        }
                    } else {
                        navController.navigate(RecoverRoutes.SPLASH_2_WELCOME) {
                            popUpTo(RecoverRoutes.SPLASH_1_LOGO) { inclusive = true }
                        }
                    }
                }
            )
        }

        // 2.jpg — Splash Welcome ("Get Started ->") Screen
        composable(RecoverRoutes.SPLASH_2_WELCOME) {
            SplashWelcomeScreen(
                onGetStartedClick = {
                    navController.navigate(RecoverRoutes.ONBOARDING_3_TO_6_HOW_IT_WORKS)
                }
            )
        }

        // 3.png, 4.png, 5.png, 6.png — Swipeable "How It Works" Carousel
        composable(RecoverRoutes.ONBOARDING_3_TO_6_HOW_IT_WORKS) {
            HowItWorksOnboardingScreen(
                creditsBalance = creditsBalance,
                cardHolderName = userSettings.displayName,
                onContinueToAuth = {
                    navController.navigate(RecoverRoutes.ONBOARDING_7_AUTH)
                }
            )
        }

        // 7.png — Sign-In / Sign-Up Screen
        composable(RecoverRoutes.ONBOARDING_7_AUTH) {
            AuthScreen(
                initialDisplayName = userSettings.displayName,
                onCompleteAuthStep = { displayName, email ->
                    coroutineScope.launch {
                        app.preferencesRepository.saveLocalProfile(displayName, email)
                        navController.navigate(RecoverRoutes.ONBOARDING_8_PRO_OFFER)
                    }
                }
            )
        }

        // 8.png — Recover Pro Offer Screen (with working "Skip ->")
        composable(RecoverRoutes.ONBOARDING_8_PRO_OFFER) {
            ProOfferScreen(
                onSkipToPermissions = {
                    navController.navigate(RecoverRoutes.ONBOARDING_9_PERMISSIONS)
                }
            )
        }

        // 9.png — Consolidated App Permissions Screen
        composable(RecoverRoutes.ONBOARDING_9_PERMISSIONS) {
            PermissionsSetupScreen(
                onFinishPermissions = {
                    navController.navigate(RecoverRoutes.ONBOARDING_10_APP_SELECTOR)
                }
            )
        }

        // 10.png — Installed Distracting Apps Selector Screen
        composable(RecoverRoutes.ONBOARDING_10_APP_SELECTOR) {
            DistractingAppsScreen(
                persistedSelections = selectedPackageSet,
                onSaveSelections = { apps ->
                    coroutineScope.launch {
                        app.repository.saveBlockedAppsSelection(apps)
                        app.preferencesRepository.setOnboardingCompleted(true)
                        navController.navigate(RecoverRoutes.DASHBOARD_HOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                onClose = {
                    if (userSettings.hasCompletedOnboarding) {
                        navController.popBackStack()
                    } else {
                        coroutineScope.launch {
                            app.preferencesRepository.setOnboardingCompleted(true)
                            navController.navigate(RecoverRoutes.DASHBOARD_HOME) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        // Dashboard Shell (`dashboard1.png`)
        composable(RecoverRoutes.DASHBOARD_HOME) {
            DashboardShellScreen(
                userSettings = userSettings,
                creditsBalance = creditsBalance,
                selectedBlockedApps = selectedBlockedApps,
                onEditBlockedApps = {
                    navController.navigate(RecoverRoutes.ONBOARDING_10_APP_SELECTOR)
                },
                onOpenProOffer = {
                    navController.navigate(RecoverRoutes.ONBOARDING_8_PRO_OFFER)
                },
                onOpenPermissions = {
                    navController.navigate(RecoverRoutes.ONBOARDING_9_PERMISSIONS)
                },
                onReplayOnboarding = {
                    navController.navigate(RecoverRoutes.SPLASH_2_WELCOME)
                }
            )
        }
    }
}
