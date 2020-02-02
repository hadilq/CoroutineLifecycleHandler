package com.github.hadilq.coroutinelifecyclehandler

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.junit.Test

class LifecycleAwareImplTest {

    @Test
    fun `in case of processor, calling observe would call handler observe`() {
        val publisher = BroadcastChannel<String>(Channel.CONFLATED)
        val handler = mock<CoroutineLifecycleHandler<String>>()
        val lifecycleAware = publisher.toLifecycleAware(handler)

        lifecycleAware.observe()

        verify(handler).observe(any(), any())
    }
}
