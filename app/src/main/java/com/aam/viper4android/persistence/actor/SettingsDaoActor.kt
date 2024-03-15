package com.aam.viper4android.persistence.actor

import com.aam.viper4android.persistence.ViPERDatabase
import com.aam.viper4android.persistence.model.PersistedSetting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDaoActor @Inject constructor(viperDatabase: ViPERDatabase) {
    private val settingsDao = viperDatabase.settingsDao()

    @OptIn(ObsoleteCoroutinesApi::class)
    private val actor = CoroutineScope(Dispatchers.IO).actor<SettingDaoMessage>(capacity = Channel.UNLIMITED) {
        for (msg in channel) {
            when (msg) {
                is SettingDaoMessage.Insert -> {
                    settingsDao.insert(msg.setting)
                }
            }
        }
    }

    fun insert(setting: PersistedSetting) {
        actor.trySend(SettingDaoMessage.Insert(setting))
    }

    private sealed class SettingDaoMessage {
        class Insert(val setting: PersistedSetting) : SettingDaoMessage()
    }
}