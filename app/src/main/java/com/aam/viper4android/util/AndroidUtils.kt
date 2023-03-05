package com.aam.viper4android.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object AndroidUtils {
    fun getApplicationLabel(context: Context, packageName: String) = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(0))
        } else {
            context.packageManager.getApplicationInfo(packageName, 0)
        }.loadLabel(context.packageManager).toString()
    } catch (e: Exception) {
        packageName
    }
}