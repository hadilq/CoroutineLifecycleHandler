package com.github.hadilq.coroutinelifecyclehandler

import com.github.hadilq.androidlifecyclehandler.SLife
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * An extension function to map [CoroutineScope] to an [Entry], so for instance make it consistent when you use [SLife].
 */
fun execute(coroutineScope: CoroutineScope, block: suspend CoroutineScope.() -> Unit) =
    Entry { coroutineScope.launch(block = block) }
