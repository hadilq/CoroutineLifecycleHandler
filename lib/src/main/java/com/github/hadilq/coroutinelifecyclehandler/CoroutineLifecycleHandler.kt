/**
 * Copyright 2019 Hadi Lashkari Ghouchani

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hadilq.coroutinelifecyclehandler

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.FlowCollector

/***
 * The base class of handlers, which glue both libraries. Notice here we assume the [LifecycleOwner]
 * needs the emitted values of upstream just between [onStart] and [onStop] events.
 */
internal class CoroutineLifecycleHandler<T> : LifecycleObserver {
    private val lifecycle by lazy { owner.lifecycle }
    private var job: Job? = null

    private lateinit var owner: LifecycleOwner
    private lateinit var entry: Entry<T>

    fun observeIn(subscribe: () -> Job):
        LifecycleOwner.() -> Unit = {
        observeEntry(Entry.ObserveInEntry(subscribe))
    }

    fun observe(subscribe: (suspend (T) -> Unit) -> Job):
        LifecycleOwner.(suspend (T) -> Unit) -> Unit = { observer ->
        observeEntry(Entry.ObserveEntry(observer, subscribe))
    }

    fun observeOnError(subscribe: (suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit) -> Job):
        LifecycleOwner.(suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit) -> Unit =
        { observer, onError ->
            observeEntry(Entry.ObserveOnErrorEntry(observer, onError, subscribe))
        }

    fun observeOnErrorOnCompletion(
        subscribe: (
            suspend (T) -> Unit,
            suspend FlowCollector<T>.(Throwable) -> Unit,
            suspend FlowCollector<T>.(Throwable?) -> Unit
        ) -> Job
    ):
        LifecycleOwner.(
            suspend (T) -> Unit,
            suspend FlowCollector<T>.(Throwable) -> Unit,
            suspend FlowCollector<T>.(Throwable?) -> Unit
        ) -> Unit =
        { observer, onError, onCompletion ->
            observeEntry(Entry.ObserveOnErrorOnCompletionEntry(observer, onError, onCompletion, subscribe))
        }

    private fun LifecycleOwner.observeEntry(entry: Entry<T>) {
        this@CoroutineLifecycleHandler.entry = entry
        this@CoroutineLifecycleHandler.owner = this
        registerIfPossible()
    }

    private fun registerIfPossible() {
        if (lifecycle.currentState != Lifecycle.State.DESTROYED) {
            lifecycle.addObserver(this)
            observeIfPossible()
        }
    }

    private fun observeIfPossible() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            job ?: let {
                job = entry.subscribe()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        observeIfPossible()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        job?.cancel()
        job = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        onStop()
        lifecycle.removeObserver(this)
    }

    sealed class Entry<T> {
        abstract fun subscribe(): Job

        class ObserveInEntry<T>(val subs: () -> Job) :
            Entry<T>() {
            override fun subscribe(): Job = subs()
        }

        class ObserveEntry<T>(
            private val observer: suspend (T) -> Unit,
            val subscribe: (suspend (T) -> Unit) -> Job
        ) :
            Entry<T>() {
            override fun subscribe(): Job = subscribe(observer)
        }

        class ObserveOnErrorEntry<T>(
            private val onEach: suspend (T) -> Unit,
            private val onError: suspend FlowCollector<T>.(Throwable) -> Unit,
            val subscribe: (suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit) -> Job
        ) :
            Entry<T>() {
            override fun subscribe(): Job = subscribe(onEach, onError)
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
        ) :
            Entry<T>() {
            override fun subscribe(): Job = subscribe(onEach, onError, onCompletion)
        }
    }
}
