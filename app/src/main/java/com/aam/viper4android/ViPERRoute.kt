package com.aam.viper4android

import androidx.mediarouter.media.MediaRouter

interface ViPERRoute {
    fun getId(): String
    fun getName(): String

    companion object {
        fun fromRouteInfo(route: MediaRouter.RouteInfo): ViPERRoute {
            return object : ViPERRoute {
                override fun getId(): String {
                    return route.id
                }

                override fun getName(): String {
                    return route.name
                }
            }
        }
    }
}