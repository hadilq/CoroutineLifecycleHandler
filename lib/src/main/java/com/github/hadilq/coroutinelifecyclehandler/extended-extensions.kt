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
import androidx.savedstate.SavedStateRegistryOwner
import com.github.hadilq.androidlifecyclehandler.AndroidExtendedLifecycleHandlerImpl
import com.github.hadilq.androidlifecyclehandler.AndroidLifecycleHandlerImpl
import com.github.hadilq.androidlifecyclehandler.ExtendedLife
import kotlinx.coroutines.CoroutineScope
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
 * The [life] is for handling the bundle in [ExtendedLife].
 * The [key] is the key which returned saved state will be associated with.
 * The [handler] to help you with dependency inversion principle.
 * The [SavedStateRegistryOwner] is the Activity or Fragment.
 */
fun <T> Flow<T>.observeIn(
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    key: String = "",
    life: ExtendedLife,
    handler: CoroutineExtendedLifecycleHandler<T> = CoroutineExtendedLifecycleHandlerImpl(
        AndroidExtendedLifecycleHandlerImpl()
    )
): SavedStateRegistryOwner.() -> Unit =
    handler.observeIn(this, scope, life, key)

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
 * The [life] is for handling the bundle in [ExtendedLife].
 * The [key] is the key which returned saved state will be associated with.
 * The [handler] to help you with dependency inversion principle.
 * The [SavedStateRegistryOwner] is the Activity or Fragment.
 */
fun <T> Flow<T>.observe(
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext), key: String = "",
    life: ExtendedLife,
    handler: CoroutineExtendedLifecycleHandler<T> = CoroutineExtendedLifecycleHandlerImpl<T>(
        AndroidExtendedLifecycleHandlerImpl()
    )
): SavedStateRegistryOwner.(suspend (T) -> Unit) -> Unit =
    handler.observe(this, scope, life, key)

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
 * The [life] is for handling the bundle in [ExtendedLife].
 * The [key] is the key which returned saved state will be associated with.
 * The [handler] to help you with dependency inversion principle.
 * The [SavedStateRegistryOwner] is the Activity or Fragment.
 */
fun <T> Flow<T>.observeOnError(
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    life: ExtendedLife,
    key: String = "",
    handler: CoroutineExtendedLifecycleHandler<T> = CoroutineExtendedLifecycleHandlerImpl(
        AndroidExtendedLifecycleHandlerImpl()
    )
): SavedStateRegistryOwner.(suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit) -> Unit =
    handler.observeOnError(this, scope, life, key)

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
 * The [life] is for handling the bundle in [ExtendedLife].
 * The [key] is the key which returned saved state will be associated with.
 * The [handler] to help you with dependency inversion principle.
 * The [SavedStateRegistryOwner] is the Activity or Fragment.
 */
fun <T> Flow<T>.observeOnErrorOnCompletion(
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    life: ExtendedLife,
    key: String = "",
    handler: CoroutineExtendedLifecycleHandler<T> = CoroutineExtendedLifecycleHandlerImpl(
        AndroidExtendedLifecycleHandlerImpl()
    )
): SavedStateRegistryOwner.(
    suspend (T) -> Unit,
    suspend FlowCollector<T>.(Throwable) -> Unit,
    suspend FlowCollector<T>.(cause: Throwable?) -> Unit
) -> Unit = handler.observeOnErrorOnCompletion(this, scope, life, key)

/***
 * To wrap up the [BroadcastChannel] and hide it from the [LifecycleOwner], which is an Activity or a
 * Fragment.
 *
 * The [handler] to help you with dependency inversion principle.
 */
fun <T> BroadcastChannel<T>.toLifecycleAware(
    handler: CoroutineLifecycleHandler<T> = CoroutineLifecycleHandlerImpl(
        AndroidLifecycleHandlerImpl()
    )
): LifecycleAware<T> = LifecycleAwareImpl(this, handler)

/***
 * To wrap up the [BroadcastChannel] and hide it from the [SavedStateRegistryOwner], which is an Activity or a
 * Fragment.
 *
 * The [handler] to help you with dependency inversion principle.
 */
inline fun <reified T : Any> BroadcastChannel<T>.toExtendedLifecycleAware(
    key: String,
    handler: CoroutineExtendedLifecycleHandler<T> = CoroutineExtendedLifecycleHandlerImpl(
        AndroidExtendedLifecycleHandlerImpl()
    )
): ExtendedLifecycleAware<T> = ExtendedLifecycleAwareImpl(this, handler, key, T::class)
