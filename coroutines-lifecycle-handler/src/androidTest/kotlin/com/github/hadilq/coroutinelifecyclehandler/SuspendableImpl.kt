package com.github.hadilq.coroutinelifecyclehandler

class SuspendableImpl<T>(private val mock: Observer<T>) : Suspendable<T> {
    override suspend fun call(s: T) = mock.call(s)
}
