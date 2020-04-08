package com.github.hadilq.coroutinelifecyclehandler

import android.os.Bundle
import com.github.hadilq.androidlifecyclehandler.ELife
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector

sealed class EEntry<T>(val life: ELife) : ELife {
    protected var job: Job? = null

    override fun onDie(): Bundle {
        job?.cancel()
        job = null
        return life.onDie()
    }

    class ObserveInEntry<T>(val subs: () -> Job, life: ELife) :
        EEntry<T>(life) {

        override fun onBorn(bundle: Bundle?) {
            life.onBorn(bundle)
            job = subs()
        }
    }

    class ObserveEntry<T>(
        private val observer: suspend (T) -> Unit,
        val subscribe: (suspend (T) -> Unit) -> Job,
        life: ELife
    ) : EEntry<T>(life) {

        override fun onBorn(bundle: Bundle?) {
            life.onBorn(bundle)
            job = subscribe(observer)
        }
    }

    class ObserveOnErrorEntry<T>(
        private val onEach: suspend (T) -> Unit,
        private val onError: suspend FlowCollector<T>.(Throwable) -> Unit,
        val subscribe: (suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit) -> Job,
        life: ELife
    ) : EEntry<T>(life) {

        override fun onBorn(bundle: Bundle?) {
            life.onBorn(bundle)
            job = subscribe(onEach, onError)
        }
    }

    class ObserveOnErrorOnCompletionEntry<T>(
        private val onEach: suspend (T) -> Unit,
        private val onError: suspend FlowCollector<T>.(Throwable) -> Unit,
        private val onCompletion: suspend FlowCollector<T>.(Throwable?) -> Unit,
        val subscribe: (
            suspend (T) -> Unit,
            suspend FlowCollector<T>.(Throwable) -> Unit,
            suspend FlowCollector<T>.(Throwable?) -> Unit
        ) -> Job,
        life: ELife
    ) : EEntry<T>(life) {

        override fun onBorn(bundle: Bundle?) {
            life.onBorn(bundle)
            job = subscribe(onEach, onError, onCompletion)
        }
    }
}
