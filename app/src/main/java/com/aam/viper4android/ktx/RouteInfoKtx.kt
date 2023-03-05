package com.aam.viper4android.ktx

import androidx.mediarouter.media.MediaRouter

fun MediaRouter.RouteInfo.getTrueUniqueId(): String {
    // TODO: Try to find a better way to get a unique id
    return id
}

fun MediaRouter.RouteInfo.getDisplayName(): String {
    return name.toString()
}