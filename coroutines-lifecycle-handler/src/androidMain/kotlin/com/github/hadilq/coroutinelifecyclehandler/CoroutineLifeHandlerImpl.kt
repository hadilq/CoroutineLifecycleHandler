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
import com.github.hadilq.androidlifecyclehandler.AndroidLifeHandler
import com.github.hadilq.androidlifecyclehandler.LifeSpan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

/***
 * An implementation of [CoroutineLifeHandler].
 */
@ExperimentalCoroutinesApi
internal class CoroutineLifeHandlerImpl<T>(private val handler: AndroidLifeHandler) :
    CoroutineLifeHandler<T> {

    override fun observeIn(
        flow: Flow<T>,
        scope: CoroutineScope
    ): LifecycleOwner.() -> Unit = {
        observeEntry(flow.toLife(scope))
    }

    override fun observe(
        flow: Flow<T>,
        scope: CoroutineScope
    ): LifecycleOwner.(suspend (T) -> Unit) -> Unit = { observer ->
        observeEntry(flow.onEach(observer).toLife(scope))
    }

    override fun observeOnError(
        flow: Flow<T>,
        scope: CoroutineScope
    ): LifecycleOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit
    ) -> Unit = { observer, onError ->
        observeEntry(flow.onEach(observer).catch(onError).toLife(scope))
    }

    override fun observeOnErrorOnCompletion(
        flow: Flow<T>,
        scope: CoroutineScope
    ): LifecycleOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit,
        suspend FlowCollector<T>.(cause: Throwable?) -> Unit
    ) -> Unit = { observer, onError, onCompletion ->
        observeEntry(flow.onEach(observer).catch(onError).onCompletion(onCompletion).toLife(scope))
    }

    private fun LifecycleOwner.observeEntry(entry: Entry) {
        handler.register(this, entry, LifeSpan.STARTED)
    }
}
