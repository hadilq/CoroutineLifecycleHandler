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
package com.github.hadilq.coroutinelifecyclehandler

import com.github.hadilq.androidlifecyclehandler.ExtendedLife
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner

@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class CoroutineExtendedLifecycleHandlerTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var mockObserver: Observer<String>

    @Mock
    private lateinit var life: ExtendedLife

    private lateinit var publisher: BroadcastChannel<String>
    private lateinit var owner: TestSavedStateRegistryOwner
    private lateinit var observer: SuspendableImpl<String>

    @Before
    fun setup() {
        owner = TestSavedStateRegistryOwner()
        publisher = BroadcastChannel(CONFLATED)
        observer = SuspendableImpl(mockObserver)
    }

    // region OBSERVE IN
    @Test
    fun `in case of just observeIn, channel should not has observer`() = runBlockingTest {
        owner.(publisher.asFlow().onEach(observer::call).observeIn(scope = this, life = life))()

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of observeIn then create then start, channel should has observer`() = runBlockingTest {
        owner.(publisher.asFlow().onEach(observer::call).observeIn(scope = this, life = life))()

        owner.create()
        owner.start()

        verifySubscription()

        owner.stop()
    }

    @Test
    fun `in case of observeIn then create then start then stop, channel should not has observer`() = runBlockingTest {
        owner.(publisher.asFlow().onEach(observer::call).observeIn(scope = this, life = life))()

        owner.create()
        owner.start()
        owner.stop()

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of observeIn then create then start then stop then start again, channel should has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().onEach(observer::call).observeIn(scope = this, life = life))()

            owner.create()
            owner.start()
            owner.stop()
            owner.start()

            verifySubscription()

            owner.stop()
        }

    @Test
    fun `in case of observeIn then create then start then destroy, channel should not has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().onEach(observer::call).observeIn(scope = this, life = life))()

            owner.create()
            owner.start()
            owner.destroy()

            verifyNoSubscription()

            owner.stop()
        }

    @Test
    fun `in case of observeIn then create then start then destroy then start, which is impossible, channel should not has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().onEach(observer::call).observeIn(scope = this, life = life))()

            owner.create()
            owner.start()
            owner.destroy()
            owner.start()

            verifyNoSubscription()

            owner.stop()
        }

    @Test
    fun `in case of destroy then observeIn, channel should not has observer`() = runBlockingTest {
        owner.destroy()

        owner.(publisher.asFlow().onEach(observer::call).observeIn(scope = this, life = life))()

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of create then start then observeIn, channel should has observer`() = runBlockingTest {
        owner.create()
        owner.start()

        owner.(publisher.asFlow().onEach(observer::call).observeIn(scope = this, life = life))()

        verifySubscription()

        owner.stop()
    }

    @Test
    fun `in case of start then stop then observeIn, channel should not has observer`() = runBlockingTest {
        owner.start()
        owner.stop()

        owner.(publisher.asFlow().onEach(observer::call).observeIn(scope = this, life = life))()

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of start then destroy then observeIn, channel should not has observer`() = runBlockingTest {
        owner.start()
        owner.destroy()

        owner.(publisher.asFlow().onEach(observer::call).observeIn(scope = this, life = life))()

        verifyNoSubscription()

        owner.stop()
    }
    // end of region OBSERVE IN

    // region OBSERVE
    @Test
    fun `in case of just observe, channel should not has observer`() = runBlockingTest {
        owner.(publisher.asFlow().observe(scope = this, life = life))(observer::call)

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of observe then create then start, channel should has observer`() = runBlockingTest {
        owner.(publisher.asFlow().observe(scope = this, life = life))(observer::call)

        owner.create()
        owner.start()

        verifySubscription()

        owner.stop()
    }

    @Test
    fun `in case of observe then create then start then stop, channel should not has observer`() = runBlockingTest {
        owner.(publisher.asFlow().observe(scope = this, life = life))(observer::call)

        owner.create()
        owner.start()
        owner.stop()

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of observe then create then start then stop then start again, channel should has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observe(scope = this, life = life))(observer::call)

            owner.create()
            owner.start()
            owner.stop()
            owner.start()

            verifySubscription()

            owner.stop()
        }

    @Test
    fun `in case of observe then create then start then destroy, channel should not has observer`() = runBlockingTest {
        owner.(publisher.asFlow().observe(scope = this, life = life))(observer::call)

        owner.create()
        owner.start()
        owner.destroy()

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of observe then create then start then destroy then start, which is impossible, channel should not has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observe(scope = this, life = life))(observer::call)

            owner.create()
            owner.start()
            owner.destroy()
            owner.start()

            verifyNoSubscription()

            owner.stop()
        }

    @Test
    fun `in case of destroy then observe, channel should not has observer`() = runBlockingTest {
        owner.destroy()

        owner.(publisher.asFlow().observe(scope = this, life = life))(observer::call)

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of create then start then observe, channel should has observer`() = runBlockingTest {
        owner.create()
        owner.start()

        owner.(publisher.asFlow().observe(scope = this, life = life))(observer::call)

        verifySubscription()

        owner.stop()
    }

    @Test
    fun `in case of start then stop then observe, channel should not has observer`() = runBlockingTest {
        owner.start()
        owner.stop()

        owner.(publisher.asFlow().observe(scope = this, life = life))(observer::call)

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of start then destroy then observe, channel should not has observer`() = runBlockingTest {
        owner.start()
        owner.destroy()

        owner.(publisher.asFlow().observe(scope = this, life = life))(observer::call)

        verifyNoSubscription()

        owner.stop()
    }
    // end of region OBSERVE

    // region OBSERVE ON ERROR
    @Test
    fun `in case of just observeOnError, channel should not has observer`() = runBlockingTest {
        owner.(publisher.asFlow().observeOnError(scope = this, life = life))(observer::call) {}

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of observeOnError then create then start, channel should has observer`() = runBlockingTest {
        owner.(publisher.asFlow().observeOnError(scope = this, life = life))(observer::call) {}

        owner.create()
        owner.start()

        verifySubscription()

        owner.stop()
    }

    @Test
    fun `in case of observeOnError then create then start then stop, channel should not has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observeOnError(scope = this, life = life))(observer::call) {}

            owner.create()
            owner.start()
            owner.stop()

            verifyNoSubscription()

            owner.stop()
        }

    @Test
    fun `in case of observeOnError then create then start then stop then start again, channel should has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observeOnError(scope = this, life = life))(observer::call) {}

            owner.create()
            owner.start()
            owner.stop()
            owner.start()

            verifySubscription()

            owner.stop()
        }

    @Test
    fun `in case of observeOnError then create then start then destroy, channel should not has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observeOnError(scope = this, life = life))(observer::call) {}

            owner.create()
            owner.start()
            owner.destroy()

            verifyNoSubscription()

            owner.stop()
        }

    @Test
    fun `in case of observeOnError then create then start then destroy then start, which is impossible, channel should not has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observeOnError(scope = this, life = life))(observer::call) {}

            owner.create()
            owner.start()
            owner.destroy()
            owner.start()

            verifyNoSubscription()

            owner.stop()
        }

    @Test
    fun `in case of destroy then observeOnError, channel should not has observer`() = runBlockingTest {
        owner.destroy()

        owner.(publisher.asFlow().observeOnError(scope = this, life = life))(observer::call) {}

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of create then start then observeOnError, channel should has observer`() = runBlockingTest {
        owner.create()
        owner.start()

        owner.(publisher.asFlow().observeOnError(scope = this, life = life))(observer::call) {}

        verifySubscription()

        owner.stop()
    }

    @Test
    fun `in case of start then stop then observeOnError, channel should not has observer`() = runBlockingTest {
        owner.start()
        owner.stop()

        owner.(publisher.asFlow().observeOnError(scope = this, life = life))(observer::call) {}

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of start then destroy then observeOnError, channel should not has observer`() = runBlockingTest {
        owner.start()
        owner.destroy()

        owner.(publisher.asFlow().observeOnError(scope = this, life = life))(observer::call) {}

        verifyNoSubscription()

        owner.stop()
    }
    // end of region OBSERVE ON ERROR

    // region OBSERVE ON ERROR ON COMPLETION
    @Test
    fun `in case of just observeOnErrorOnCompletion, channel should not has observer`() = runBlockingTest {
        owner.(publisher.asFlow().observeOnErrorOnCompletion(scope = this, life = life))(observer::call, {}) {}

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of observeOnErrorOnCompletion then create then start, channel should has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observeOnErrorOnCompletion(scope = this, life = life))(observer::call, {}) {}

            owner.create()
            owner.start()

            verifySubscription()

            owner.stop()
        }

    @Test
    fun `in case of observeOnErrorOnCompletion then create then start then stop, channel should not has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observeOnErrorOnCompletion(scope = this, life = life))(observer::call, {}) {}

            owner.create()
            owner.start()
            owner.stop()

            verifyNoSubscription()

            owner.stop()
        }

    @Test
    fun `in case of observeOnErrorOnCompletion then create then start then stop then start again, channel should has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observeOnErrorOnCompletion(scope = this, life = life))(observer::call, {}) {}

            owner.create()
            owner.start()
            owner.stop()
            owner.start()

            verifySubscription()

            owner.stop()
        }

    @Test
    fun `in case of observeOnErrorOnCompletion then create then start then destroy, channel should not has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observeOnErrorOnCompletion(scope = this, life = life))(observer::call, {}) {}

            owner.create()
            owner.start()
            owner.destroy()

            verifyNoSubscription()

            owner.stop()
        }

    @Test
    fun `in case of observeOnErrorOnCompletion then create then start then destroy then start, which is impossible, channel should not has observer`() =
        runBlockingTest {
            owner.(publisher.asFlow().observeOnErrorOnCompletion(scope = this, life = life))(observer::call, {}) {}

            owner.create()
            owner.start()
            owner.destroy()
            owner.start()

            verifyNoSubscription()

            owner.stop()
        }

    @Test
    fun `in case of destroy then observeOnErrorOnCompletion, channel should not has observer`() = runBlockingTest {
        owner.destroy()

        owner.(publisher.asFlow().observeOnErrorOnCompletion(scope = this, life = life))(observer::call, {}) {}

        verifyNoSubscription()

        owner.stop()
    }

    @Test
    fun `in case of create then start then observeOnErrorOnCompletion, channel should has observer`() =
        runBlockingTest {
            owner.create()
            owner.start()

            owner.(publisher.asFlow().observeOnErrorOnCompletion(scope = this, life = life))(observer::call, {}) {}

            verifySubscription()

            owner.stop()
        }

    @Test
    fun `in case of create then start then stop then observeOnErrorOnCompletion, channel should not has observer`() =
        runBlockingTest {
            owner.create()
            owner.start()
            owner.stop()

            owner.(publisher.asFlow().observeOnErrorOnCompletion(scope = this, life = life))(observer::call, {}) {}

            verifyNoSubscription()

            owner.stop()
        }

    @Test
    fun `in case of create then start then destroy then observeOnErrorOnCompletion, channel should not has observer`() =
        runBlockingTest {
            owner.create()
            owner.start()
            owner.destroy()

            owner.(publisher.asFlow().observeOnErrorOnCompletion(scope = this, life = life))(observer::call, {}) {}

            verifyNoSubscription()

            owner.stop()
        }
    // end of region OBSERVE ON ERROR ON COMPLETION

    private suspend fun verifySubscription() {
        publisher.send("A")
        verify(mockObserver).call("A")
    }

    private suspend fun verifyNoSubscription() {
        publisher.send("A")
        verify(mockObserver, never()).call("A")
    }
}
