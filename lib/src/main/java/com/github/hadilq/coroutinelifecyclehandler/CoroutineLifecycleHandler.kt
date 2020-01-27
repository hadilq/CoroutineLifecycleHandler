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

import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.EmptyCoroutineContext

/***
 * A class to handle lifecycle of subscription and unsubscription of a [Flow]. Notice here we assume the
 * [LifecycleOwner] needs the emitted values of upstream just between [ON_START] and [ON_STOP] events.
 */
interface CoroutineLifecycleHandler<T> {

    /***
     * Creates a handler to sync the subscription.
     *
     * The [flow] is the upstream.
     * The [scope] is an optional Scope to have more control on the cancellations.
     * The [LifecycleOwner] is the Activity or Fragment.
     */
    fun observeIn(
        flow: Flow<T>,
        scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    ): LifecycleOwner.() -> Unit

    /***
     * Creates a handler to sync the subscription.
     *
     * The [flow] is the upstream.
     * The [scope] is an optional Scope to have more control on the cancellations.
     * The [LifecycleOwner] is the Activity or Fragment.
     */
    fun observe(
        flow: Flow<T>,
        scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    ): LifecycleOwner.(suspend (T) -> Unit) -> Unit

    /***
     * Creates a handler to sync the subscription.
     *
     * The [flow] is the upstream.
     * The [scope] is an optional Scope to have more control on the cancellations.
     * The [LifecycleOwner] is the Activity or Fragment.
     */
    fun observeOnError(
        flow: Flow<T>,
        scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)
    ): LifecycleOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit
    ) -> Unit

    /***
     * Creates a handler to sync the subscription.
     *
     * The [flow] is the upstream.
     * The [scope] is an optional Scope to have more control on the cancellations.
     * The [LifecycleOwner] is the Activity or Fragment.
     */
    fun observeOnErrorOnCompletion(flow: Flow<T>, scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)):
        LifecycleOwner.(
            suspend (T) -> Unit,
            suspend FlowCollector<T>.(Throwable) -> Unit,
            suspend FlowCollector<T>.(cause: Throwable?) -> Unit
        ) -> Unit
}
