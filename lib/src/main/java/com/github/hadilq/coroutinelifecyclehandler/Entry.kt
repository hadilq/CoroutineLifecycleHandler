package com.github.hadilq.coroutinelifecyclehandler

import com.github.hadilq.androidlifecyclehandler.Life
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector

sealed class Entry<T> : Life {
    protected var job: Job? = null

    override fun onDie() {
        job?.cancel()
        job = null
    }

    class ObserveInEntry<T>(val subs: () -> Job) : Entry<T>() {

        override fun onBorn() {
            job = subs()
        }
    }

    class ObserveEntry<T>(
        private val observer: suspend (T) -> Unit,
        val subscribe: (suspend (T) -> Unit) -> Job
    ) : Entry<T>() {

        override fun onBorn() {
            job = subscribe(observer)
        }
    }

    class ObserveOnErrorEntry<T>(
        private val onEach: suspend (T) -> Unit,
        private val onError: suspend FlowCollector<T>.(Throwable) -> Unit,
        val subscribe: (suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit) -> Job
    ) : Entry<T>() {

        override fun onBorn() {
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
        ) -> Job
    ) : Entry<T>() {

        override fun onBorn() {
            job = subscribe(onEach, onError, onCompletion)
        }
    }
}
