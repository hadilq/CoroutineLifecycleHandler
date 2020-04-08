package com.github.hadilq.coroutinelifecyclehandler.sample

import androidx.lifecycle.ViewModel
import com.github.hadilq.coroutinelifecyclehandler.toELifeAware
import com.github.hadilq.coroutinelifecyclehandler.toLifeAware
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED

class MainViewModel : ViewModel() {

    private val publisher = BroadcastChannel<String>(CONFLATED)
    private val extendedPublisher = BroadcastChannel<String>(CONFLATED)

    /**
     * This emitter would NOT be saved on onSaveInstanceState
     */
    val stringEmitter = publisher.toLifeAware()

    /**
     * This emitter would be saved on onSaveInstanceState
     */
    val extendedStringEmitter = extendedPublisher.toELifeAware(KEY)

    companion object {
        private const val KEY = "key_to_save_string_emitter"
    }
}
