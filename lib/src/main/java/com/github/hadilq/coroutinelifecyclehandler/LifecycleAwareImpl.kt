package com.github.hadilq.coroutinelifecyclehandler

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow

class LifecycleAwareImpl<T>(
    private val channel: BroadcastChannel<T>,
    private val handler: CoroutineLifecycleHandler<T>
) : LifecycleAware<T> {

    override fun observe(scope: CoroutineScope): LifecycleOwner.(
        suspend (T) -> Unit
    ) -> Unit = handler.observe(channel.asFlow(), scope)
}
