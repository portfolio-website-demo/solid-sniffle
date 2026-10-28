package com.recover.app.platform.permissions

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.recover.app.domain.model.PermissionCapability
import com.recover.app.domain.model.PermissionStatus
import com.recover.app.platform.enforcement.RecoverEnforcementAccessibilityService

object PermissionChecker {

    fun checkAllPermissions(context: Context): Map<PermissionCapability, PermissionStatus> {
        val now = System.currentTimeMillis()
        return mapOf(
            PermissionCapability.ACCESSIBILITY_ENFORCEMENT to PermissionStatus(
                capability = PermissionCapability.ACCESSIBILITY_ENFORCEMENT,
                isGranted = isAccessibilityServiceEnabled(context),
                checkedAtEpochMs = now
            ),
            PermissionCapability.NOTIFICATIONS to PermissionStatus(
                capability = PermissionCapability.NOTIFICATIONS,
                isGranted = areNotificationsEnabled(context),
                checkedAtEpochMs = now
            ),
            PermissionCapability.OVERLAY_DISPLAY to PermissionStatus(
                capability = PermissionCapability.OVERLAY_DISPLAY,
                isGranted = isOverlayPermissionGranted(context),
                checkedAtEpochMs = now
            ),
            PermissionCapability.CAMERA_AR_PRESENCE to PermissionStatus(
                capability = PermissionCapability.CAMERA_AR_PRESENCE,
                isGranted = isCameraPermissionGranted(context),
                checkedAtEpochMs = now
            )
        )
    }

    fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val expectedComponent = ComponentName(context, RecoverEnforcementAccessibilityService::class.java)
        val enabledServicesSetting = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (!enabledServicesSetting.isNullOrBlank()) {
            val colonSplitter = enabledServicesSetting.split(':')
            for (componentNameString in colonSplitter) {
                val enabledComponent = ComponentName.unflattenFromString(componentNameString)
                if (enabledComponent != null && enabledComponent == expectedComponent) {
                    return true
                }
            }
        }

        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager
        val enabledList = am?.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            ?: emptyList()
        return enabledList.any { info ->
            val serviceInfo = info.resolveInfo?.serviceInfo
            serviceInfo?.packageName == context.packageName &&
                serviceInfo?.name == RecoverEnforcementAccessibilityService::class.java.name
        }
    }

    fun areNotificationsEnabled(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val runtimeGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!runtimeGranted) return false
        }
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun isOverlayPermissionGranted(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun isCameraPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun createAccessibilitySettingsIntent(): Intent {
        return Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun createOverlayPermissionIntent(context: Context): Intent {
        return Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun createNotificationSettingsIntent(context: Context): Intent {
        return Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
