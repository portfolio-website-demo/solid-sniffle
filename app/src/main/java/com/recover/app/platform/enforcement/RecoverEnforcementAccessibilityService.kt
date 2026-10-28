package com.recover.app.platform.enforcement

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import com.recover.app.MainActivity
import com.recover.app.RecoverApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Real Android OS enforcement AccessibilityService.
 * Detects foreground window package transitions and enforces blocking for selected distracting apps
 * unless a valid unexpired 60-second UnlockGrant exists in Room.
 */
class RecoverEnforcementAccessibilityService : AccessibilityService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val ignoredSystemPackages = setOf(
        "com.android.systemui",
        "com.android.settings",
        "com.android.phone",
        "com.android.server.telecom",
        "com.google.android.inputmethod.latin",
        "com.google.android.permissioncontroller",
        "com.android.permissioncontroller"
    )

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            event.eventType != AccessibilityEvent.TYPE_WINDOWS_CHANGED
        ) {
            return
        }

        val foregroundPkg = event.packageName?.toString()
            ?: rootInActiveWindow?.packageName?.toString()
            ?: return

        if (foregroundPkg == packageName || foregroundPkg in ignoredSystemPackages) {
            return
        }

        val app = applicationContext as? RecoverApplication ?: return
        val repository = app.repository

        serviceScope.launch(Dispatchers.IO) {
            val selectedBlockedPackages = repository.getSelectedBlockedPackageSet()
            if (foregroundPkg !in selectedBlockedPackages) {
                return@launch
            }

            val nowElapsed = SystemClock.elapsedRealtime()
            val nowEpoch = System.currentTimeMillis()
            val isUnlocked = repository.isPackageTemporarilyUnlocked(
                packageName = foregroundPkg,
                nowElapsedRealtimeMs = nowElapsed,
                nowEpochMs = nowEpoch
            )

            if (!isUnlocked && EnforcementStateHolder.isBlockingEnforced.value) {
                // Enforce OS block: navigate user out of the blocked app and present Recover interception
                performGlobalAction(GLOBAL_ACTION_HOME)
                val interceptIntent = Intent(this@RecoverEnforcementAccessibilityService, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra(EXTRA_INTERCEPTED_PACKAGE, foregroundPkg)
                }
                startActivity(interceptIntent)
            }
        }
    }

    override fun onInterrupt() {
        // Required override for AccessibilityService
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val EXTRA_INTERCEPTED_PACKAGE = "extra_intercepted_package"
    }
}

object EnforcementStateHolder {
    val isBlockingEnforced = kotlinx.coroutines.flow.MutableStateFlow(false)
}
