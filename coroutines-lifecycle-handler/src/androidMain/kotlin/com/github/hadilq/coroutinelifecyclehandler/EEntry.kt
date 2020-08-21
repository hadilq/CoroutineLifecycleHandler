package com.github.hadilq.coroutinelifecyclehandler

import android.os.Bundle
import com.github.hadilq.androidlifecyclehandler.ELife
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlin.coroutines.EmptyCoroutineContext

class EEntry(val subs: () -> Job, private val life: ELife) : ELife {

    private var job: Job? = null

    override fun onBorn(bundle: Bundle?) {
        life.onBorn(bundle)
        job = subs()
    }

    override fun onDie(): Bundle {
        job?.cancel()
        job = null
        return life.onDie()
    }
}

/**
 * Builder function to create an [ELife] to be able to sync easily. It needs another [life] to be able to handle the
 * Bundle. Also it needs an [scope] to include external cancellation.
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.toELife(
    life: ELife,
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
) = EEntry({ launchIn(scope) }, life)
