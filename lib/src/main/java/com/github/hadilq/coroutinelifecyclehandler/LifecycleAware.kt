package com.github.hadilq.coroutinelifecyclehandler

import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlin.coroutines.EmptyCoroutineContext

/***
 * Uses for hiding [BroadcastChannel] from [LifecycleOwner] and prepare it for observation by [LifecycleOwner].
 */
interface LifecycleAware<T> {

    /**
     * Observe the wrapped up [BroadcastChannel].
     *
     * Example of use:
     * ```
     * class MyAndroidActivity : ComponentActivity {
     *
     *   override fun onCreate(savedInstanceState: Bundle?) {
     *
     *       (viewModel.stringEmitter.observe()) { testString ->
     *           assert(testString == "Test")
     *       }
     *   }
     * }
     * ```
     *
     * The [scope] is an optional Scope to have more control on the cancellations.
     * The [LifecycleOwner] is the Activity or Fragment.
     */
    fun observe(scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)): LifecycleOwner.(
        suspend (T) -> Unit
    ) -> Unit
}
