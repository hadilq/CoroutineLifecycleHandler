package com.github.hadilq.coroutinelifecyclehandler

import com.github.hadilq.androidlifecyclehandler.Life
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlin.coroutines.EmptyCoroutineContext

class Entry(val subs: () -> Job) : Life {

    private var job: Job? = null

    override fun onBorn() {
        job = subs()
    }

    override fun onDie() {
        job?.cancel()
        job = null
    }
}

/**
 * Builder function to create a [Life] to be able to sync easily. It needs an [scope] to include external cancellation.
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.toLife(
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
) = Entry { launchIn(scope) }
