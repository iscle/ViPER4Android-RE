package com.aam.viper4android

import android.content.Context

class AndroidUtils {
    companion object {
        fun getApplicationLabel(context: Context, packageName: String) = try {
            context.packageManager.getApplicationLabel(context.packageManager.getApplicationInfo(packageName, 0)).toString()
        } catch (e: Exception) {
            packageName
        }
    }
}