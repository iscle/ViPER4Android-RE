package com.aam.viper4android.persistence.actor

import com.aam.viper4android.persistence.ViPERDatabase
import com.aam.viper4android.persistence.model.SavedSession
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionDaoActor @Inject constructor(viperDatabase: ViPERDatabase) {
    private val sessionDao = viperDatabase.sessionDao()

    @OptIn(ObsoleteCoroutinesApi::class)
    private val actor = CoroutineScope(Dispatchers.IO).actor<SessionDaoMessage>(capacity = Channel.UNLIMITED) {
        for (msg in channel) {
            when (msg) {
                is SessionDaoMessage.Insert -> {
                    sessionDao.insert(msg.session)
                }
                is SessionDaoMessage.Delete -> {
                    sessionDao.delete(msg.packageName, msg.sessionId)
                }
            }
        }
    }

    fun close() {
        actor.close()
    }

    fun insert(session: SavedSession) {
        actor.trySend(SessionDaoMessage.Insert(session))
    }

    fun delete(packageName: String, sessionId: Int) {
        actor.trySend(SessionDaoMessage.Delete(packageName, sessionId))
    }

    private sealed class SessionDaoMessage {
        class Insert(val session: SavedSession) : SessionDaoMessage()
        class Delete(val packageName: String, val sessionId: Int) : SessionDaoMessage()
    }
}