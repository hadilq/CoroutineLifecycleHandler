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

import androidx.lifecycle.LifecycleOwner
import com.github.hadilq.androidlifecyclehandler.AndroidLifecycleHandler
import com.github.hadilq.androidlifecyclehandler.LifeSpan
import com.github.hadilq.coroutinelifecyclehandler.Entry.ObserveEntry
import com.github.hadilq.coroutinelifecyclehandler.Entry.ObserveInEntry
import com.github.hadilq.coroutinelifecyclehandler.Entry.ObserveOnErrorEntry
import com.github.hadilq.coroutinelifecyclehandler.Entry.ObserveOnErrorOnCompletionEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

/***
 * An implementation of [CoroutineLifecycleHandler].
 */
internal class CoroutineLifecycleHandlerImpl<T>(private val handler: AndroidLifecycleHandler) :
    CoroutineLifecycleHandler<T> {

    override fun observeIn(
        flow: Flow<T>,
        scope: CoroutineScope
    ): LifecycleOwner.() -> Unit = observeIn(flow.onLaunchIn(scope))

    override fun observe(
        flow: Flow<T>,
        scope: CoroutineScope
    ): LifecycleOwner.(suspend (T) -> Unit) -> Unit = observe(flow.onEachLaunchIn(scope))

    override fun observeOnError(
        flow: Flow<T>,
        scope: CoroutineScope
    ): LifecycleOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit
    ) -> Unit = observeOnError(flow.onEachOnErrorLaunchIn(scope))

    override fun observeOnErrorOnCompletion(
        flow: Flow<T>,
        scope: CoroutineScope
    ): LifecycleOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit,
        suspend FlowCollector<T>.(cause: Throwable?) -> Unit
    ) -> Unit = observeOnErrorOnCompletion(flow.onEachOnErrorOnCompletionLaunchIn(scope))

    private fun observeIn(subscribe: () -> Job):
        LifecycleOwner.() -> Unit = {
        observeEntry(ObserveInEntry(subscribe))
    }

    private fun observe(subscribe: (suspend (T) -> Unit) -> Job):
        LifecycleOwner.(suspend (T) -> Unit) -> Unit = { observer ->
        observeEntry(ObserveEntry(observer, subscribe))
    }

    private fun observeOnError(
        subscribe: (
            suspend (T) -> Unit,
            suspend FlowCollector<T>.(Throwable) -> Unit
        ) -> Job
    ): LifecycleOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit
    ) -> Unit = { observer, onError ->
        observeEntry(ObserveOnErrorEntry(observer, onError, subscribe))
    }

    private fun observeOnErrorOnCompletion(
        subscribe: (
            suspend (T) -> Unit,
            suspend FlowCollector<T>.(Throwable) -> Unit,
            suspend FlowCollector<T>.(Throwable?) -> Unit
        ) -> Job
    ): LifecycleOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit,
        suspend FlowCollector<T>.(Throwable?) -> Unit
    ) -> Unit = { observer, onError, onCompletion ->
        observeEntry(ObserveOnErrorOnCompletionEntry(observer, onError, onCompletion, subscribe))
    }

    private fun LifecycleOwner.observeEntry(entry: Entry<T>) {
        handler.register(this, entry, LifeSpan.STARTED)
    }

    private fun <T> Flow<T>.onLaunchIn(scope: CoroutineScope): () -> Job = { launchIn(scope) }

    private fun <T> Flow<T>.onEachLaunchIn(scope: CoroutineScope):
            (suspend (T) -> Unit) -> Job = { observer ->
        onEach(observer).launchIn(scope)
    }

    private fun <T> Flow<T>.onEachOnErrorLaunchIn(scope: CoroutineScope):
            (suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit) -> Job = { observer, onError ->
        onEach(observer).catch(onError).launchIn(scope)
    }

    private fun <T> Flow<T>.onEachOnErrorOnCompletionLaunchIn(scope: CoroutineScope): (
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit,
        suspend FlowCollector<T>.(cause: Throwable?) -> Unit
    ) -> Job =
        { observer, onError, onCompletion ->
            onEach(observer).catch(onError).onCompletion(onCompletion).launchIn(scope)
        }
}
