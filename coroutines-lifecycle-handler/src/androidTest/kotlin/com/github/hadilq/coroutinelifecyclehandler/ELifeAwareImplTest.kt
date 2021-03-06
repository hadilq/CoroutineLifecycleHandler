package com.github.hadilq.coroutinelifecyclehandler

import android.os.Bundle
import android.os.Parcelable
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@FlowPreview
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class ELifeAwareImplTest {

    @Test
    fun `In case of born of Bundle LifecycleAware, load`() {
        `test loading`<Bundle> { putBundle(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of Bundle LifecycleAware, save the cache`() {
        `test saving` { getBundle(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of Parcelable LifecycleAware, load`() {
        `test loading`<Parcelable> { putParcelable(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of Parcelable LifecycleAware, save the cache`() {
        `test saving` { getParcelable<Parcelable>(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of String LifecycleAware, load`() {
        `test loading`("A") { putString(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of String LifecycleAware, save the cache`() {
        `test saving`("A") { getString(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of ArrayList String LifecycleAware, load`() {
        `test loading`<ArrayList<String>> { putStringArrayList(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of ArrayList String LifecycleAware, save the cache`() {
        `test saving` { getStringArrayList(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of CharSequence LifecycleAware, load`() {
        `test loading`<CharSequence> { putCharSequence(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of CharSequence LifecycleAware, save the cache`() {
        `test saving` { getCharSequence(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of Byte LifecycleAware, load`() {
        `test loading`<Byte>(2, true) { putByte(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of Byte LifecycleAware, save the cache`() {
        `test saving`(2, true) { getByte(ELifeAwareImpl.KEY) }
    }

    @Test
    fun `In case of born of Short LifecycleAware, load`() {
        `test loading`<Short>(1, true) { putShort(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of Short LifecycleAware, save the cache`() {
        `test saving`(1, true) { getShort(ELifeAwareImpl.KEY) }
    }

    @Test
    fun `In case of born of Char LifecycleAware, load`() {
        `test loading`('a', true) { putChar(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of Char LifecycleAware, save the cache`() {
        `test saving`('a', true) { getChar(ELifeAwareImpl.KEY) }
    }

    @Test
    fun `In case of born of Int LifecycleAware, load`() {
        `test loading`(1, true) { putInt(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of Int LifecycleAware, save the cache`() {
        `test saving`(1, true) { getInt(ELifeAwareImpl.KEY) }
    }

    @Test
    fun `In case of born of Long LifecycleAware, load`() {
        `test loading`(1L, true) { putLong(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of Long LifecycleAware, save the cache`() {
        `test saving`(1L, true) { getLong(ELifeAwareImpl.KEY) }
    }

    @Test
    fun `In case of born of Float LifecycleAware, load`() {
        `test loading`(0.1f, true) { putFloat(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of Float LifecycleAware, save the cache`() {
        `test saving`(0.2f, true) { getFloat(ELifeAwareImpl.KEY) }
    }

    @Test
    fun `In case of born of Double LifecycleAware, load`() {
        `test loading`(0.2, true) { putDouble(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of Double LifecycleAware, save the cache`() {
        `test saving`(0.1, true) { getDouble(ELifeAwareImpl.KEY) }
    }

    @Test
    fun `In case of born of Boolean LifecycleAware, load`() {
        `test loading`(value = true, supportAutoBoxing = true) { putBoolean(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of Boolean LifecycleAware, save the cache`() {
        `test saving`(value = true, supportAutoBoxing = true) { getBoolean(ELifeAwareImpl.KEY) }
    }

    @Test
    fun `In case of born of Array Parcelable LifecycleAware, load`() {
        `test loading`<Array<out Parcelable?>>(Array(0) { mock<Parcelable>() }) {
            putParcelableArray(ELifeAwareImpl.KEY, it)
        }
    }

    @Test
    fun `In case of die of Array Parcelable LifecycleAware, save the cache`() {
        `test saving`<Array<out Parcelable?>>(Array(0) { mock<Parcelable>() }) {
            getParcelableArray(ELifeAwareImpl.KEY)!!
        }
    }

    @Test
    fun `In case of born of Array String LifecycleAware, load`() {
        `test loading`<Array<out String?>>(Array(0) { "" }) {
            putStringArray(ELifeAwareImpl.KEY, it)
        }
    }

    @Test
    fun `In case of die of Array String LifecycleAware, save the cache`() {
        `test saving`<Array<out String?>>(Array(0) { "" }) {
            getStringArray(ELifeAwareImpl.KEY)!!
        }
    }

    @Test
    fun `In case of born of Array CharSequence LifecycleAware, load`() {
        `test loading`<Array<out CharSequence?>>(Array(0) { mock<CharSequence>() }) {
            putCharSequenceArray(ELifeAwareImpl.KEY, it)
        }
    }

    @Test
    fun `In case of die of Array CharSequence LifecycleAware, save the cache`() {
        `test saving`<Array<out CharSequence?>>(Array(0) { mock<CharSequence>() }) {
            getCharSequenceArray(ELifeAwareImpl.KEY)!!
        }
    }

    @Test
    fun `In case of born of ByteArray LifecycleAware, load`() {
        `test loading`(ByteArray(0)) { putByteArray(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of ByteArray LifecycleAware, save the cache`() {
        `test saving`(ByteArray(0)) { getByteArray(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of ShortArray LifecycleAware, load`() {
        `test loading`(ShortArray(0)) { putShortArray(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of ShortArray LifecycleAware, save the cache`() {
        `test saving`(ShortArray(0)) { getShortArray(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of CharArray LifecycleAware, load`() {
        `test loading`(CharArray(0)) { putCharArray(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of CharArray LifecycleAware, save the cache`() {
        `test saving`(CharArray(0)) { getCharArray(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of IntArray LifecycleAware, load`() {
        `test loading`(IntArray(0)) { putIntArray(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of IntArray LifecycleAware, save the cache`() {
        `test saving`(IntArray(0)) { getIntArray(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of LongArray LifecycleAware, load`() {
        `test loading`(LongArray(0)) { putLongArray(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of LongArray LifecycleAware, save the cache`() {
        `test saving`(LongArray(0)) { getLongArray(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of FloatArray LifecycleAware, load`() {
        `test loading`(FloatArray(0)) { putFloatArray(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of FloatArray LifecycleAware, save the cache`() {
        `test saving`(FloatArray(0)) { getFloatArray(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of DoubleArray LifecycleAware, load`() {
        `test loading`(DoubleArray(0)) { putDoubleArray(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of DoubleArray LifecycleAware, save the cache`() {
        `test saving`(DoubleArray(0)) { getDoubleArray(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of BooleanArray LifecycleAware, load`() {
        `test loading`(BooleanArray(0)) { putBooleanArray(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of BooleanArray LifecycleAware, save the cache`() {
        `test saving`(BooleanArray(0)) { getBooleanArray(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of ArrayList Parcelable LifecycleAware, load`() {
        `test loading`<ArrayList<Parcelable>> { putParcelableArrayList(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of ArrayList Parcelable LifecycleAware, save the cache`() {
        `test saving` { getParcelableArrayList<Parcelable>(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of ArrayList Int LifecycleAware, load`() {
        `test loading`<ArrayList<Int>> { putIntegerArrayList(ELifeAwareImpl.KEY, it) }
    }

    @Test
    fun `In case of die of ArrayList Int LifecycleAware, save the cache`() {
        `test saving` { getIntegerArrayList(ELifeAwareImpl.KEY)!! }
    }

    @Test
    fun `In case of born of UnsupportedData LifecycleAware, load`() {
        try {
            `test loading`(UnsupportedData(false)) { }
            throw AssertionError("An exception must be thrown")
        } catch (e: IllegalArgumentException) {
        }
    }

    @Test
    fun `In case of die of UnsupportedData LifecycleAware, save the cache`() {
        try {
            `test saving`(UnsupportedData(false)) { UnsupportedData(false) }
            throw AssertionError("An exception must be thrown")
        } catch (e: IllegalArgumentException) {
        }
    }

    private inline fun <reified T : Any> `test loading`(
        value: T = mock(),
        supportAutoBoxing: Boolean = false,
        crossinline putter: Bundle.(T) -> Unit
    ) = runBlockingTest {
        val publisher = BroadcastChannel<T>(CONFLATED)
        val handler: CoroutineELifeHandler<T> = mock()
        val lifecycleAware = publisher.toELifeAware(KEY, handler)
        lifecycleAware.observe(this)

        lifecycleAware.onBorn(Bundle().apply { putter(value) })

        // To cache a value
        val captor = argumentCaptor<Flow<T>>()
        verify(handler).observe(captor.capture(), any(), eq(lifecycleAware), eq(KEY))
        var result: T? = null
        launch {
            captor.firstValue.collect { result = it }
        }

        if (supportAutoBoxing) {
            assert(result == value)
        } else {
            assert(result === value)
        }
        publisher.cancel()
    }

    private inline fun <reified T : Any> `test saving`(
        value: T = mock(),
        supportAutoBoxing: Boolean = false,
        crossinline getter: Bundle.() -> T
    ) = runBlockingTest {
        val publisher = BroadcastChannel<T>(CONFLATED)
        val handler: CoroutineELifeHandler<T> = mock()
        val lifecycleAware = publisher.toELifeAware(KEY, handler)
        lifecycleAware.observe(this)

        publisher.send(value)

        // To cache a value
        val captor = argumentCaptor<Flow<T>>()
        verify(handler).observe(captor.capture(), any(), eq(lifecycleAware), eq(KEY))
        launch {
            captor.firstValue.collect { }
        }

        val result = lifecycleAware.onDie()

        if (supportAutoBoxing) {
            assert(result.run { getter() } == value)
        } else {
            assert(result.run { getter() } === value)
        }
        publisher.cancel()
    }

    @Test
    fun `in case of processor, calling observe would call handler observe`() {
        val publisher = BroadcastChannel<String>(CONFLATED)
        val handler = mock<CoroutineELifeHandler<String>>()
        val lifecycleAware = publisher.toELifeAware(KEY, handler)

        lifecycleAware.observe()

        verify(handler).observe(any(), any(), any(), eq(KEY))
    }

    data class UnsupportedData(val unknown: Boolean)
    companion object {
        private const val KEY = "EXTENDED_LIFECYCLE_AWARE_TEST_KEY"
    }
}
