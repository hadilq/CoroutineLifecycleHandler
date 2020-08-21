package com.github.hadilq.coroutinelifecyclehandler

interface Suspendable<T> {
    suspend fun call(s: T)
}
