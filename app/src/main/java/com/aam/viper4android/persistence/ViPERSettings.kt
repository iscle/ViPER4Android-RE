package com.aam.viper4android.persistence

import com.aam.viper4android.persistence.actor.SettingsDaoActor
import com.aam.viper4android.persistence.model.PersistedSetting
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViPERSettings @Inject constructor(
    viperDatabase: ViPERDatabase,
    private val settingsDaoActor: SettingsDaoActor,
) {
    private val KEY_LEGACY_MODE = "legacy_mode"

    private val settingsDao = viperDatabase.settingsDao()
    private val settings = mutableMapOf<String, String>()
    private val listeners = mutableListOf<Listener>()

    init {
        runBlocking {
            settingsDao.getAll().forEach { settings[it.key] = it.value }
        }
    }

    fun addListener(listener: Listener) {
        if (listeners.contains(listener)) return
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    private fun get(key: String): String? = settings[key]
    private fun set(key: String, value: String) {
        settings[key] = value
        settingsDaoActor.insert(PersistedSetting(key, value))
    }

    var isLegacyMode: Boolean
        get() = get(KEY_LEGACY_MODE)?.toBoolean() ?: false
        set(value) {
            set(KEY_LEGACY_MODE, value.toString())
            listeners.forEach { it.onIsLegacyModeChanged(this, value) }
        }

    interface Listener {
        fun onIsLegacyModeChanged(viperSettings: ViPERSettings, isLegacyMode: Boolean)
    }
}