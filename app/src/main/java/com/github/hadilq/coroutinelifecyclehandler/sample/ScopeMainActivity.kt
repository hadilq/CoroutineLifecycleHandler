package com.github.hadilq.coroutinelifecyclehandler.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.github.hadilq.coroutinelifecyclehandler.observe
import com.github.hadilq.coroutinelifecyclehandler.observeIn
import com.github.hadilq.coroutinelifecyclehandler.observeOnError
import com.github.hadilq.coroutinelifecyclehandler.observeOnErrorOnCompletion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class ScopeMainActivity : ComponentActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val flow = flow { emit("A") }

        // Flow usage
        flow
            .onEach { }
            .catch { }
            .onCompletion { }
            .observeIn(this)()
        (flow.observe(this))(::handleString)
        (flow.observeOnError(this))(::handleString, handleError())
        (flow.observeOnErrorOnCompletion(this))(::handleString, handleError(), handleCompletion())
    }

    private suspend fun handleString(@Suppress("UNUSED_PARAMETER") s: String) {
    }

    private fun handleError(): suspend FlowCollector<String>.(Throwable) -> Unit = {
    }

    private fun handleCompletion(): suspend FlowCollector<String>.(Throwable?) -> Unit = {
    }
}
