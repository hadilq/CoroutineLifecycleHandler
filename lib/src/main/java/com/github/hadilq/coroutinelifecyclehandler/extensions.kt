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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext

/***
 * Creates a handler to sync the subscription.
 *
 * Example of use:
 * ```
 * class MyAndroidActivity : ComponentActivity {
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *
 *       flow
 *           .onEach { }
 *           .catch { }
 *           .onCompletion { }
 *           .observeIn()()
 *   }
 * }
 *
 * ```
 *
 */
fun <T> Flow<T>.observeIn(): LifecycleOwner.() -> Unit = observeIn(CoroutineScope(EmptyCoroutineContext))

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
 */
fun <T> Flow<T>.observeIn(scope: CoroutineScope):
    LifecycleOwner.() -> Unit = CoroutineLifecycleHandler<T>().observeIn(onLaunchIn(scope))

/***
 * Creates a handler to sync the subscription.
 *
 * Example of use:
 * ```
 * class MyAndroidActivity : ComponentActivity {
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *
 *       (flow.observe(this))(::handleString)
 *   }
 * }
 *
 * ```
 *
 */
fun <T> Flow<T>.observe():
    LifecycleOwner.(suspend (T) -> Unit) -> Unit = observe(CoroutineScope(EmptyCoroutineContext))

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
 */
fun <T> Flow<T>.observe(scope: CoroutineScope):
    LifecycleOwner.(suspend (T) -> Unit) -> Unit =
    CoroutineLifecycleHandler<T>()
        .observe(onEachLaunchIn(scope))

/***
 * Creates a handler to sync the subscription.
 *
 * Example of use:
 * ```
 * class MyAndroidActivity : ComponentActivity {
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *
 *       (flow.observeOnError())(::handleString, handleError())
 *   }
 * }
 *
 * ```
 *
 */
fun <T> Flow<T>.observeOnError():
    LifecycleOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit
    ) -> Unit = observeOnError(CoroutineScope(EmptyCoroutineContext))

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
 */
fun <T> Flow<T>.observeOnError(scope: CoroutineScope):
    LifecycleOwner.(suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit) -> Unit =
    CoroutineLifecycleHandler<T>()
        .observeOnError(onEachOnErrorLaunchIn(scope))

/***
 * Creates a handler to sync the subscription.
 *
 * Example of use:
 * ```
 * class MyAndroidActivity : ComponentActivity {
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *
 *       (flow.observeOnErrorOnCompletion())(::handleString, handleError(), handleCompletion())
 *   }
 * }
 *
 * ```
 *
 */
fun <T> Flow<T>.observeOnErrorOnCompletion():
    LifecycleOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit,
        suspend FlowCollector<T>.(cause: Throwable?) -> Unit
    ) -> Unit = observeOnErrorOnCompletion(CoroutineScope(EmptyCoroutineContext))

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
 */
fun <T> Flow<T>.observeOnErrorOnCompletion(scope: CoroutineScope):
    LifecycleOwner.(
        suspend (T) -> Unit,
        suspend FlowCollector<T>.(Throwable) -> Unit,
        suspend FlowCollector<T>.(cause: Throwable?) -> Unit
    ) -> Unit =
    CoroutineLifecycleHandler<T>()
        .observeOnErrorOnCompletion(onEachOnErrorOnCompletionLaunchIn(scope))

private fun <T> Flow<T>.onLaunchIn(scope: CoroutineScope): () -> Job = { launchIn(scope) }

private fun <T> Flow<T>.onEachLaunchIn(scope: CoroutineScope):
        (suspend (T) -> Unit) -> Job = { observer ->
    onEach(observer).launchIn(scope)
}

private fun <T> Flow<T>.onEachOnErrorLaunchIn(scope: CoroutineScope):
        (suspend (T) -> Unit, suspend FlowCollector<T>.(Throwable) -> Unit) -> Job = { observer, onError ->
    onEach(observer).catch(onError).launchIn(scope)
}

private fun <T> Flow<T>.onEachOnErrorOnCompletionLaunchIn(scope: CoroutineScope):
        (
    suspend (T) -> Unit,
    suspend FlowCollector<T>.(Throwable) -> Unit,
    suspend FlowCollector<T>.(cause: Throwable?) -> Unit
) -> Job =
    { observer, onError, onCompletion ->
        onEach(observer).catch(onError).onCompletion(onCompletion).launchIn(scope)
    }

