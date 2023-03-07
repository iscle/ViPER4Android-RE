package com.aam.viper4android.persistence.actor

import com.aam.viper4android.persistence.ViPERDatabase
import com.aam.viper4android.persistence.model.PersistedSession
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionsDaoActor @Inject constructor(viperDatabase: ViPERDatabase) {
    private val sessionsDao = viperDatabase.sessionsDao()

    @OptIn(ObsoleteCoroutinesApi::class)
    private val actor = CoroutineScope(Dispatchers.IO).actor<SessionsDaoMessage>(capacity = Channel.UNLIMITED) {
        for (msg in channel) {
            when (msg) {
                is SessionsDaoMessage.Insert -> {
                    sessionsDao.insert(msg.session)
                }
                is SessionsDaoMessage.Delete -> {
                    sessionsDao.delete(msg.packageName, msg.sessionId)
                }
            }
        }
    }

    fun insert(session: PersistedSession) {
        actor.trySend(SessionsDaoMessage.Insert(session))
    }

    fun delete(packageName: String, sessionId: Int) {
        actor.trySend(SessionsDaoMessage.Delete(packageName, sessionId))
    }

    private sealed class SessionsDaoMessage {
        class Insert(val session: PersistedSession) : SessionsDaoMessage()
        class Delete(val packageName: String, val sessionId: Int) : SessionsDaoMessage()
    }
}