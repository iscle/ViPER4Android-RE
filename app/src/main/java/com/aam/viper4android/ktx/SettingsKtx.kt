package com.aam.viper4android.ktx

import android.content.ContentResolver
import android.provider.Settings

fun getBootCount(contentResolver: ContentResolver): Int {
    return Settings.Global.getInt(contentResolver, Settings.Global.BOOT_COUNT)
}