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
package com.github.hadilq.coroutinelifecyclehandler.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.github.hadilq.androidlifecyclehandler.ExtendedLife
import com.github.hadilq.coroutinelifecyclehandler.observe
import com.github.hadilq.coroutinelifecyclehandler.observeIn
import com.github.hadilq.coroutinelifecyclehandler.observeOnError
import com.github.hadilq.coroutinelifecyclehandler.observeOnErrorOnCompletion
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    private val life = object : ExtendedLife {
        override fun onBorn(bundle: Bundle?) {
        }

        override fun onDie(): Bundle = Bundle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val flow = flow { emit("A") }

        // Flow usage
        flow
            .onEach { }
            .catch { }
            .onCompletion { }
            .observeIn()()

        (flow.observe())(::handleString)
        (flow.observeOnError())(::handleString, handleError())
        (flow.observeOnErrorOnCompletion())(::handleString, handleError(), handleCompletion())

        // OR
        flow
            .onEach { }
            .catch { }
            .onCompletion { }
            .observeIn(life, KEY)()

        (flow.observe(life, KEY))(::handleString)
        (flow.observeOnError(life, KEY))(::handleString, handleError())
        (flow.observeOnErrorOnCompletion(life, KEY))(::handleString, handleError(), handleCompletion())
    }

    private suspend fun handleString(@Suppress("UNUSED_PARAMETER") s: String) {
    }

    private fun handleError(): suspend FlowCollector<String>.(Throwable) -> Unit = {
    }

    private fun handleCompletion(): suspend FlowCollector<String>.(Throwable?) -> Unit = {
    }

    companion object {
        private const val KEY = "key_to_save_the_data"
    }
}
