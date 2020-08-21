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

import androidx.savedstate.SavedStateRegistryOwner
import com.github.hadilq.androidlifecyclehandler.AndroidELifeHandlerImpl
import com.github.hadilq.androidlifecyclehandler.ELife
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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
 * The [life] is for handling the bundle in [ELife].
 * The [key] is the key which returned saved state will be associated with.
 * The [scope] is an optional Scope to have more control on the cancellations.
 * The [handler] to help you with dependency inversion principle.
 * The [SavedStateRegistryOwner] is the Activity or Fragment.
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.observeIn(
    life: ELife,
    key: String = "",
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    handler: CoroutineELifeHandler<T> = CoroutineELifeHandlerImpl(
        AndroidELifeHandlerImpl()
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
 * The [life] is for handling the bundle in [ELife].
 * The [key] is the key which returned saved state will be associated with.
 * The [scope] is an optional Scope to have more control on the cancellations.
 * The [handler] to help you with dependency inversion principle.
 * The [SavedStateRegistryOwner] is the Activity or Fragment.
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.observe(
    life: ELife,
    key: String = "",
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    handler: CoroutineELifeHandler<T> = CoroutineELifeHandlerImpl(
        AndroidELifeHandlerImpl()
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
 * The [life] is for handling the bundle in [ELife].
 * The [key] is the key which returned saved state will be associated with.
 * The [scope] is an optional Scope to have more control on the cancellations.
 * The [handler] to help you with dependency inversion principle.
 * The [SavedStateRegistryOwner] is the Activity or Fragment.
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.observeOnError(
    life: ELife,
    key: String = "",
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    handler: CoroutineELifeHandler<T> = CoroutineELifeHandlerImpl(
        AndroidELifeHandlerImpl()
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
 * The [life] is for handling the bundle in [ELife].
 * The [key] is the key which returned saved state will be associated with.
 * The [scope] is an optional Scope to have more control on the cancellations.
 * The [handler] to help you with dependency inversion principle.
 * The [SavedStateRegistryOwner] is the Activity or Fragment.
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.observeOnErrorOnCompletion(
    life: ELife,
    key: String = "",
    scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext),
    handler: CoroutineELifeHandler<T> = CoroutineELifeHandlerImpl(
        AndroidELifeHandlerImpl()
    )
): SavedStateRegistryOwner.(
    suspend (T) -> Unit,
    suspend FlowCollector<T>.(Throwable) -> Unit,
    suspend FlowCollector<T>.(cause: Throwable?) -> Unit
) -> Unit = handler.observeOnErrorOnCompletion(this, scope, life, key)

/***
 * To wrap up the [BroadcastChannel] and hide it from the [SavedStateRegistryOwner], which is an Activity or a
 * Fragment.
 *
 * Example of use:
 * ```
 * class MyViewModel : ViewModel() {
 *
 *   private val extendedPublisher = BroadcastChannel<String>(CONFLATED)
 *   val extendedStringEmitter = extendedPublisher.toELifeAware(KEY)
 *
 *   companion object {
 *       private const val KEY = "key_to_save_string_emitter"
 *   }
 * }
 * ```
 *
 * The [key] is the key which returned saved state will be associated with.
 * The [handler] to help you with dependency inversion principle.
 */
@ExperimentalCoroutinesApi
@FlowPreview
inline fun <reified T : Any> BroadcastChannel<T>.toELifeAware(
    key: String,
    handler: CoroutineELifeHandler<T> = CoroutineELifeHandlerImpl(
        AndroidELifeHandlerImpl()
    )
): ELifeAware<T> = ELifeAwareImpl(this, handler, key, T::class)
