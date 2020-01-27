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

import androidx.savedstate.SavedStateRegistryOwner
import com.github.hadilq.androidlifecyclehandler.AndroidExtendedLifecycleHandler
import com.github.hadilq.androidlifecyclehandler.ExtendedLife
import com.github.hadilq.androidlifecyclehandler.LifeSpan
import com.github.hadilq.coroutinelifecyclehandler.ExtendedEntry.ObserveEntry
import com.github.hadilq.coroutinelifecyclehandler.ExtendedEntry.ObserveInEntry
import com.github.hadilq.coroutinelifecyclehandler.ExtendedEntry.ObserveOnErrorEntry
import com.github.hadilq.coroutinelifecyclehandler.ExtendedEntry.ObserveOnErrorOnCompletionEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

/***
 * An implementation of [CoroutineExtendedLifecycleHandler].
 */
class CoroutineExtendedLifecycleHandlerImpl<T>(private val handler: AndroidExtendedLifecycleHandler) :
    CoroutineExtendedLifecycleHandler<T> {

    override fun observeIn(
        flow: Flow<T>,
        scope: CoroutineScope,
        life: ExtendedLife,
        key: String
    ): SavedStateRegistryOwner.() -> Unit = observeIn(flow.onLaunchIn(scope), life, key)

    override fun observe(
        flow: Flow<T>,
        scope: CoroutineScope,
        life: ExtendedLife,
        key: String
    ): SavedStateRegistryOwner.(suspend (T) -> Unit) -> Unit = observe(flow.onEachLaunchIn(scope), life, key)

    override fun observeOnError(
        flow: Flow<T>,
        scope: CoroutineScope,
        life: ExtendedLife,
        key: String
    ): SavedStateRegistryOwner.(suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit) -> Unit =
        observeOnError(flow.onEachOnErrorLaunchIn(scope), life, key)

    override fun observeOnErrorOnCompletion(
        flow: Flow<T>,
        scope: CoroutineScope,
        life: ExtendedLife,
        key: String
    ): SavedStateRegistryOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit,
        suspend FlowCollector<T>.(cause: Throwable?) -> Unit
    ) -> Unit = observeOnErrorOnCompletion(flow.onEachOnErrorOnCompletionLaunchIn(scope), life, key)

    private fun observeIn(
        subscribe: () -> Job,
        life: ExtendedLife,
        key: String
    ): SavedStateRegistryOwner.() -> Unit = {
        observeEntry(ObserveInEntry(subscribe, life), key)
    }

    private fun observe(
        subscribe: (suspend (T) -> Unit) -> Job,
        life: ExtendedLife,
        key: String
    ): SavedStateRegistryOwner.(suspend (T) -> Unit) -> Unit = { observer ->
        observeEntry(ObserveEntry(observer, subscribe, life), key)
    }

    private fun observeOnError(
        subscribe: (
            suspend (T) -> Unit,
            suspend FlowCollector<T>.(Throwable) -> Unit
        ) -> Job,
        life: ExtendedLife,
        key: String
    ): SavedStateRegistryOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit
    ) -> Unit = { observer, onError ->
        observeEntry(ObserveOnErrorEntry(observer, onError, subscribe, life), key)
    }

    private fun observeOnErrorOnCompletion(
        subscribe: (
            suspend (T) -> Unit,
            suspend FlowCollector<T>.(Throwable) -> Unit,
            suspend FlowCollector<T>.(Throwable?) -> Unit
        ) -> Job,
        life: ExtendedLife,
        key: String
    ): SavedStateRegistryOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit,
        suspend FlowCollector<T>.(Throwable?) -> Unit
    ) -> Unit = { observer, onError, onCompletion ->
        observeEntry(ObserveOnErrorOnCompletionEntry(observer, onError, onCompletion, subscribe, life), key)
    }

    private fun SavedStateRegistryOwner.observeEntry(entry: ExtendedEntry<T>, key: String) {
        handler.register(this, entry, LifeSpan.STARTED, key)
    }

    private fun <T> Flow<T>.onLaunchIn(scope: CoroutineScope): () -> Job = { launchIn(scope) }

    private fun <T> Flow<T>.onEachLaunchIn(scope: CoroutineScope): (suspend (T) -> Unit) -> Job = { observer ->
        onEach(observer).launchIn(scope)
    }

    private fun <T> Flow<T>.onEachOnErrorLaunchIn(scope: CoroutineScope): (
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit
    ) -> Job = { observer, onError ->
        onEach(observer).catch(onError).launchIn(scope)
    }

    private fun <T> Flow<T>.onEachOnErrorOnCompletionLaunchIn(scope: CoroutineScope): (
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit,
        suspend FlowCollector<T>.(cause: Throwable?) -> Unit
    ) -> Job = { observer, onError, onCompletion ->
        onEach(observer).catch(onError).onCompletion(onCompletion).launchIn(scope)
    }
}
