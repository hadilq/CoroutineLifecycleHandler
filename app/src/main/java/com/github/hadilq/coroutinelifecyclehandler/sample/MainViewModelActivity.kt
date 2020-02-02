package com.github.hadilq.coroutinelifecyclehandler.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider

class MainViewModelActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        (viewModel.stringEmitter.observe()) { testString ->
            /* use it here */
        }
        (viewModel.extendedStringEmitter.observe()) { testString ->
            /* use it here */
        }
    }
}
