package com.aam.viper4android

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViPERManager @Inject constructor(@ApplicationContext private val context: Context) {

    fun getActiveSessions(): List<ActiveSession> {
        return listOf(
            ActiveSession("com.spotify.music", 0),
            ActiveSession("com.google.android.youtube", 1),
            ActiveSession("com.google.android.youtube.music", 2),
            ActiveSession("com.google.android.apps.youtube.music", 3),
            ActiveSession("com.google.android.apps.youtube.kids", 4),
            ActiveSession("com.google.android.apps.youtube.unplugged", 5),
            ActiveSession("com.google.android.apps.youtube.creator", 6),
            ActiveSession("com.google.android.apps.youtube.gaming", 7),
            ActiveSession("com.google.android.apps.youtube.musicpremium", 8),
            ActiveSession("com.google.android.apps.youtube.vr", 9),
            ActiveSession("com.google.android.apps.youtube.kids", 10),
            ActiveSession("com.google.android.apps.youtube.unplugged", 11),
            ActiveSession("com.google.android.apps.youtube.creator", 12),
            ActiveSession("com.google.android.apps.youtube.gaming", 13),
            ActiveSession("com.google.android.apps.youtube.musicpremium", 14),
        )
    }
}