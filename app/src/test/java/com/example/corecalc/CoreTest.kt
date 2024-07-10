package com.example.corecalc

import android.util.Log
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CoreTest {

    @Mock
    private lateinit var logger: LoggerInterface

    //@Mock
    private lateinit var core: Core

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        //logger = LogWrapper()
        core = Core(logger = logger)
        /*whenever(core.solve("3+2")).thenReturn("5")
        whenever(core.solve("3-0.1")).thenReturn("2.9")*/
        //verify(logger).d(anyString(), anyString())  ???
    }

    @Test
    fun addition() {
        val result = core.solve("3+2")
        assertEquals("5", result)
        verify(logger).d("expr", "3+2")
    }

    @Test
    fun additionIncomplete() {
        val result = core.solve("3+")
        assertEquals("3", result)
        verify(logger).d("expr", "3+")
    }

    @Test
    fun additionResultIntegerFromFloatingPointNumber() {
        val result = core.solve("2.5+2.5")
        assertEquals("5", result)
        verify(logger).d("expr", "2.5+2.5")
    }

    @Test
    fun subtractionResultGreaterThanZero() {
        val result = core.solve("3-2")
        assertEquals("1", result)
        verify(logger).d("expr", "3-2")
    }

    @Test
    fun subtractionResultLessThanZero() {
        val result = core.solve("2-3")
        assertEquals("-1", result)
        verify(logger).d("expr", "2-3")
    }

    @Test
    fun subtractionResultFloat() {
        val result = core.solve("3-0.1")
        assertEquals("2.9", result)
        verify(logger).d("expr", "3-0.1")
    }

    @Test
    fun multiplicationResultGreaterThanZero() {
        val result = core.solve("3*2")
        assertEquals("6", result)
        verify(logger).d("expr", "3*2")
    }

    @Test
    fun multiplicationResultLessThanZero() {
        val result = core.solve("-3*2")
        assertEquals("-6", result)
        verify(logger).d("expr", "-3*2")
    }

    @Test
    fun multiplicationResultFloat() {
        val result = core.solve("3*0.5")
        assertEquals("1.5", result)
        verify(logger).d("expr", "3*0.5")
    }

    @Test
    fun multiplicationIncomplete() {
        val result = core.solve("3*")
        assertEquals("3", result)
        verify(logger).d("expr", "3*")
    }

    @Test
    fun divisionResultGreaterThanZero() {
        val result = core.solve("3/2")
        assertEquals("1.5", result)
        verify(logger).d("expr", "3/2")
    }

    @Test
    fun divisionResultLessThanZero() {
        val result = core.solve("-3/2")
        assertEquals("-1.5", result)
        verify(logger).d("expr", "-3/2")
    }

    @Test
    fun powerResultGreaterThanZero() {
        val result = core.solve("2^3")
        assertEquals("8", result)
        verify(logger).d("expr", "2^3")
    }

    @Test
    fun powerResultLessThanZero() {
        val result = core.solve("2^(-3)")
        assertEquals("-8", result)
        verify(logger).d("expr", "2^(-3)")
    }

    @Test
    fun powerFloat() {
        val result = core.solve("4^0.5")
        assertEquals("2", result)
        verify(logger).d("expr", "4^0.5")
    }

    @Test
    fun powerResultFloat() {
        val result = core.solve("4^(-1)")
        assertEquals("0.25", result)
        verify(logger).d("expr", "4^(-1)")
    }
}