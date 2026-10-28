package com.recover.app.platform.apps

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.recover.app.domain.model.AppCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class InstalledAppInfo(
    val packageName: String,
    val label: String,
    val category: AppCategory,
    val iconBitmap: ImageBitmap?
)

object InstalledAppDiscovery {

    private val EXCLUDED_SYSTEM_PACKAGES = setOf(
        "com.android.settings",
        "com.android.systemui",
        "com.android.phone",
        "com.android.server.telecom",
        "com.google.android.PermissionController"
    )

    /**
     * Queries the actual Android PackageManager for launchable installed applications.
     * Never returns hardcoded apps.
     */
    suspend fun queryEligibleInstalledApps(context: Context): List<InstalledAppInfo> =
        withContext(Dispatchers.IO) {
            val pm = context.packageManager
            val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }

            val resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL)
            val ownPackage = context.packageName

            resolveInfos
                .mapNotNull { resolveInfo ->
                    val activityInfo = resolveInfo.activityInfo ?: return@mapNotNull null
                    val pkg = activityInfo.packageName ?: return@mapNotNull null
                    if (pkg == ownPackage || pkg in EXCLUDED_SYSTEM_PACKAGES) {
                        return@mapNotNull null
                    }
                    val appInfo = activityInfo.applicationInfo
                    val label = runCatching {
                        resolveInfo.loadLabel(pm).toString().trim()
                    }.getOrDefault(pkg).ifEmpty { pkg }

                    val iconBitmap = runCatching {
                        val drawable = resolveInfo.loadIcon(pm)
                        drawableToImageBitmap(drawable, targetSizePx = 128)
                    }.getOrNull()

                    val category = classifyAppCategory(pkg, label, appInfo)

                    InstalledAppInfo(
                        packageName = pkg,
                        label = label,
                        category = category,
                        iconBitmap = iconBitmap
                    )
                }
                .distinctBy { it.packageName }
                .sortedWith(
                    compareBy<InstalledAppInfo> { it.category.ordinal }
                        .thenBy { it.label.lowercase() }
                )
        }

    private fun classifyAppCategory(
        packageName: String,
        label: String,
        appInfo: ApplicationInfo?
    ): AppCategory {
        val osCategory = appInfo?.category ?: ApplicationInfo.CATEGORY_UNDEFINED
        when (osCategory) {
            ApplicationInfo.CATEGORY_SOCIAL -> return AppCategory.SOCIAL
            ApplicationInfo.CATEGORY_VIDEO,
            ApplicationInfo.CATEGORY_AUDIO,
            ApplicationInfo.CATEGORY_IMAGE -> return AppCategory.VIDEO_ENTERTAINMENT
            ApplicationInfo.CATEGORY_GAME -> return AppCategory.GAMES
        }

        val token = "${packageName.lowercase()} ${label.lowercase()}"
        return when {
            listOf(
                "instagram", "tiktok", "musically", "zhiliaoapp", "facebook", "katana",
                "whatsapp", "snapchat", "twitter", "threads", "reddit", "linkedin",
                "messenger", "orca", "discord", "telegram", "pinterest", "messaging", "chat"
            ).any { it in token } -> AppCategory.SOCIAL

            listOf(
                "youtube", "netflix", "twitch", "primevideo", "disney", "spotify",
                "video", "player", "photos", "gallery", "chrome", "browser"
            ).any { it in token } -> AppCategory.VIDEO_ENTERTAINMENT

            listOf("game", "play.games").any { it in token } -> AppCategory.GAMES

            else -> AppCategory.PRODUCTIVITY_OTHER
        }
    }

    private fun drawableToImageBitmap(drawable: Drawable, targetSizePx: Int): ImageBitmap {
        if (drawable is BitmapDrawable && drawable.bitmap != null) {
            return drawable.bitmap.asImageBitmap()
        }
        val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: targetSizePx
        val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: targetSizePx
        val bmp = Bitmap.createBitmap(
            width.coerceAtMost(targetSizePx),
            height.coerceAtMost(targetSizePx),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bmp.asImageBitmap()
    }
}
