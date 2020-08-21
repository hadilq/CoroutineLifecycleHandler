package com.github.hadilq.coroutinelifecyclehandler

import com.github.hadilq.androidlifecyclehandler.DLife
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlin.coroutines.EmptyCoroutineContext

open class DEntry<DNA>(val subs: () -> Job, private val life: DLife<DNA>) : DLife<DNA> {

    private var job: Job? = null

    override fun onBorn(dna: DNA?) {
        life.onBorn(dna)
        job = subs()
    }

    override fun onDie(): DNA {
        job?.cancel()
        job = null
        return life.onDie()
    }
}

/**
 * Builder function to create an [DLife] to be able to sync easily. It needs another [life] to be able to handle the
 * Bundle. Also it needs an [scope] to include external cancellation.
 */
@ExperimentalCoroutinesApi
fun <T, D> Flow<T>.toDLife(
    life: DLife<D>,
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
) = DEntry({ launchIn(scope) }, life)
