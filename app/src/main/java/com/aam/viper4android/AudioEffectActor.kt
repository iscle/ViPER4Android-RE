package com.aam.viper4android

import android.media.audiofx.AudioEffect
import com.aam.viper4android.ktx.getParameterKtx
import com.aam.viper4android.ktx.setParameterKtx
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor

class AudioEffectActor(private val audioEffect: AudioEffect) {
    @OptIn(ObsoleteCoroutinesApi::class)
    private val actor = CoroutineScope(Dispatchers.IO).actor<EffectMessage>(capacity = Channel.UNLIMITED) {
        for (msg in channel) {
            when (msg) {
                is EffectMessage.GetEnabled -> {
                    msg.result.complete(audioEffect.enabled)
                }
                is EffectMessage.SetEnabled -> {
                    audioEffect.enabled = msg.enabled
                }
                is EffectMessage.GetParameter -> {
                    val result = audioEffect.getParameterKtx(msg.param, msg.value)
                    msg.result?.complete(result)
                }
                is EffectMessage.SetParameter -> {
                    val result = audioEffect.setParameterKtx(msg.param, msg.value)
                    msg.result?.complete(result)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun close(handler: ((cause: Throwable?) -> Unit)? = null) {
        handler?.let { actor.invokeOnClose(it) }
        actor.close()
    }

    fun getEnabled(result: CompletableDeferred<Boolean>) {
        actor.trySend(EffectMessage.GetEnabled(result))
    }

    fun setEnabled(enabled: Boolean) {
        actor.trySend(EffectMessage.SetEnabled(enabled))
    }

    fun getParameter(param: ByteArray, value: ByteArray, result: CompletableDeferred<Int>? = null) {
        actor.trySend(EffectMessage.GetParameter(param, value, result))
    }

    fun setParameter(param: ByteArray, value: ByteArray, result: CompletableDeferred<Int>? = null) {
        actor.trySend(EffectMessage.SetParameter(param, value, result))
    }

    private sealed class EffectMessage {
        class GetEnabled(val result: CompletableDeferred<Boolean>) : EffectMessage()
        class SetEnabled(val enabled: Boolean) : EffectMessage()
        class GetParameter(val param: ByteArray, val value: ByteArray, val result: CompletableDeferred<Int>? = null) : EffectMessage()
        class SetParameter(val param: ByteArray, val value: ByteArray, val result: CompletableDeferred<Int>? = null) : EffectMessage()
    }
}