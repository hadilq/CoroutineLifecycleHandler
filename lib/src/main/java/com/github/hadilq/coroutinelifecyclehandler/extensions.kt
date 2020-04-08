/***
 * Copyright 2019 Hadi Lashkari Ghouchani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */

package com.github.hadilq.coroutinelifecyclehandler

import androidx.lifecycle.LifecycleOwner
import com.github.hadilq.androidlifecyclehandler.AndroidLifeHandlerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.EmptyCoroutineContext

/***
 * Creates a handler to sync the subscription.
 *
 * Example of use:
 * ```
 * class MyAndroidActivity : ComponentActivity, CoroutineScope by MainScope(){
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *
 *       flow
 *           .onEach { }
 *           .catch { }
 *           .onCompletion { }
 *           .observeIn(this)()
 *   }
 * }
 *
 * ```
 *
 * The [Flow] is the upstream.
 * The [scope] is an optional Scope to have more control on the cancellations.
 * The [handler] to help you with dependency inversion principle.
 * The [LifecycleOwner] is the Activity or Fragment.
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.observeIn(
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    handler: CoroutineLifeHandler<T> = CoroutineLifeHandlerImpl(AndroidLifeHandlerImpl())
): LifecycleOwner.() -> Unit = handler.observeIn(this, scope)

/***
 * Creates a handler to sync the subscription.
 *
 * Example of use:
 * ```
 * class MyAndroidActivity : ComponentActivity, CoroutineScope by MainScope(){
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *
 *       flow
 *           .onEach { }
 *           .catch { }
 *           .onCompletion { }
 *           .observe(this)
 *   }
 * }
 *
 * ```
 *
 * The [Flow] is the upstream.
 * The [scope] is an optional Scope to have more control on the cancellations.
 * The [handler] to help you with dependency inversion principle.
 * The [LifecycleOwner] is the Activity or Fragment.
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.observe(
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    handler: CoroutineLifeHandler<T> = CoroutineLifeHandlerImpl(AndroidLifeHandlerImpl())
): LifecycleOwner.(
    suspend (T) -> Unit
) -> Unit = handler.observe(this, scope)

/***
 * Creates a handler to sync the subscription.
 *
 * Example of use:
 * ```
 * class MyAndroidActivity : ComponentActivity, CoroutineScope by MainScope() {
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *
 *       (flow.observeOnError(this))(::handleString, handleError())
 *   }
 * }
 *
 * ```
 *
 * The [Flow] is the upstream.
 * The [scope] is an optional Scope to have more control on the cancellations.
 * The [handler] to help you with dependency inversion principle.
 * The [LifecycleOwner] is the Activity or Fragment.
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.observeOnError(
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    handler: CoroutineLifeHandler<T> = CoroutineLifeHandlerImpl(AndroidLifeHandlerImpl())
): LifecycleOwner.(
    suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit
) -> Unit = handler.observeOnError(this, scope)

/***
 * Creates a handler to sync the subscription.
 *
 * Example of use:
 * ```
 * class MyAndroidActivity : ComponentActivity, CoroutineScope by MainScope() {
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *
 *       (flow.observeOnErrorOnCompletion(this))(::handleString, handleError(), handleCompletion())
 *   }
 * }
 *
 * ```
 *
 * The [Flow] is the upstream.
 * The [scope] is an optional Scope to have more control on the cancellations.
 * The [handler] to help you with dependency inversion principle.
 * The [LifecycleOwner] is the Activity or Fragment.
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.observeOnErrorOnCompletion(
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    handler: CoroutineLifeHandler<T> = CoroutineLifeHandlerImpl(
        AndroidLifeHandlerImpl()
    )
): LifecycleOwner.(
    suspend (T) -> Unit,
    suspend FlowCollector<T>.(Throwable) -> Unit,
    suspend FlowCollector<T>.(cause: Throwable?) -> Unit
) -> Unit = handler.observeOnErrorOnCompletion(this, scope)

/***
 * To wrap up the [BroadcastChannel] and hide it from the [LifecycleOwner], which is an Activity or a
 * Fragment.
 *
 * Example of use:
 * ```
 * class MyViewModel : ViewModel() {
 *
 *   private val publisher = BroadcastChannel<String>(CONFLATED)
 *   val stringEmitter = publisher.toLifecycleAware()
 * }
 * ```
 *
 * The [handler] to help you with dependency inversion principle.
 */
@ExperimentalCoroutinesApi
fun <T> BroadcastChannel<T>.toLifeAware(
    handler: CoroutineLifeHandler<T> = CoroutineLifeHandlerImpl(
        AndroidLifeHandlerImpl()
    )
): LifeAware<T> = LifeAwareImpl(this, handler)
