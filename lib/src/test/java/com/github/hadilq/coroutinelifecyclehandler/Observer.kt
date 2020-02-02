package com.github.hadilq.coroutinelifecyclehandler

interface Observer<T> {
    fun call(s: T)
}
