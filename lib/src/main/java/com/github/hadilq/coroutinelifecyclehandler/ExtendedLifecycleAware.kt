package com.github.hadilq.coroutinelifecyclehandler

import androidx.savedstate.SavedStateRegistryOwner
import com.github.hadilq.androidlifecyclehandler.ExtendedLife
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlin.coroutines.EmptyCoroutineContext

/***
 * Uses for hiding [BroadcastChannel] from [SavedStateRegistryOwner] and prepare it for observation by
 * [SavedStateRegistryOwner].
 *
 * Example of use:
 * ```
 * class MyAndroidActivity : ComponentActivity {
 *
 *   override fun onCreate(savedInstanceState: Bundle?) {
 *
 *       (viewModel.extendedStringEmitter.observe()) { testString ->
 *           assert(testString == "Test")
 *       }
 *   }
 * }
 * ```
 */
interface ExtendedLifecycleAware<T> : ExtendedLife {

    /**
     * Observe the wrapped up [BroadcastChannel].
     *
     * The [scope] is an optional Scope to have more control on the cancellations.
     * The [SavedStateRegistryOwner] is the Activity or Fragment.
     */
    fun observe(scope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)): SavedStateRegistryOwner.(
        suspend (T) -> Unit
    ) -> Unit
}
