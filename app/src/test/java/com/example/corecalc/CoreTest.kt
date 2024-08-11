package com.example.corecalc

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
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
        //whenever(core.solve("3+2")).thenReturn("5")
        //verify(logger).d(anyString(), anyString())  ???
    }

    @Test
    fun addition() {
        val result = core.solve("3+2")
        assertEquals("5", result)
    }

    @Test
    fun additionIncomplete() {
        val result = core.solve("3+")
        assertEquals("3", result)
    }

    @Test
    fun additionResultIntegerFromFloatingPointNumber() {
        val result = core.solve("2.5+2.5")
        assertEquals("5", result)
    }

    @Test
    fun subtractionResultGreaterThanZero() {
        val result = core.solve("3-2")
        assertEquals("1", result)
    }

    @Test
    fun subtractionResultNegative() {
        val result = core.solve("2-3")
        assertEquals("-1", result)
    }

    @Test
    fun subtractionResultFloat() {
        val result = core.solve("3-0.1")
        assertEquals("2.9", result)
    }

    @Test
    fun substractionIncompleteWithClosingParenthesis() {
        val result = core.solve("55-)")
        assertEquals("Error", result)
    }

    @Test
    fun substractionIncomplete() {
        val result = core.solve("55-")
        assertEquals("55", result)
    }

    @Test
    fun multiplicationResultGreaterThanZero() {
        val result = core.solve("3*2")
        assertEquals("6", result)
    }

    @Test
    fun multiplicationResultNegative() {
        val result = core.solve("-3*2")
        assertEquals("-6", result)
    }

    @Test
    fun multiplicationResultFloat() {
        val result = core.solve("3*0.5")
        assertEquals("1.5", result)
    }

    @Test
    fun multiplicationIncomplete() {
        val result = core.solve("3*")
        assertEquals("3", result)
    }

    @Test
    fun divisionResultGreaterThanZero() {
        val result = core.solve("3/2")
        assertEquals("1.5", result)
    }

    @Test
    fun divisionResultNegative() {
        val result = core.solve("-3/2")
        assertEquals("-1.5", result)
    }

    // FIXME знак "∞" получать из константы класса
    @Test
    fun divisionByZero() {
        val result = core.solve("5/0")
        assertEquals("∞", result)
    }

    @Test
    fun powerResultGreaterThanZero() {
        val result = core.solve("2^3")
        assertEquals("8", result)
    }

    @Test
    fun powerNegative() {
        val result = core.solve("2^(-3)")
        assertEquals("0.125", result)
    }

    @Test
    fun powerFloat() {
        val result = core.solve("4^0.5")
        assertEquals("2", result)
    }

    @Test
    fun powerResultFloat() {
        val result = core.solve("4^(-1)")
        assertEquals("0.25", result)
    }

    @Test
    fun twoOperatorsPriorityOrder1() {
        val result = core.solve("3+2*5")
        assertEquals("13", result)
    }

    @Test
    fun twoOperatorsPriorityOrder2() {
        val result = core.solve("10/2+3")
        assertEquals("8", result)
    }

    @Test
    fun negativeNumberSubtraction() {
        val result = core.solve("2+(-3")
        assertEquals("-1", result)
    }

    @Test
    fun negativeNumberPower() {
        val result = core.solve("-2^3")
        assertEquals("-8", result)
    }

    @Test
    fun complexExpression() {
        val result = core.solve("((12.5+8)-3.2)*(10/2.5)^2+(5-2)*(4.75/1.5)")
        assertEquals("286.3", result)
    }

    @Test
    fun incompleteInputHandle() {
        val result = core.solve("7-)")
        assertEquals("Error", result)
    }

    @Test
    fun emptyInput() {
        // core.solve("") не подразумевается при нормальной работе через UI
        val result = core.solve("")     // 0
        assertEquals("0", result)
    }

    @Test
    fun dotInput() {
        // core.solve("") не подразумевается при нормальной работе через UI
        val result = core.solve(".")    // 0.
        assertEquals("Error", result)
    }

    @Test
    fun plusInput() {
        // core.solve("") не подразумевается при нормальной работе через UI
        val result = core.solve("+")    // 0+
        assertEquals("Error", result)
    }

    // FIXME 
    /*@Test
    fun minusInput() {
        // core.solve("") не подразумевается при нормальной работе через UI
        val result = core.solve("-")    // 0-
        assertEquals("Error", result)   // 0 так как возле минуса подставляется 0
    }*/

    @Test
    fun multiplicationInput() {
        // core.solve("") не подразумевается при нормальной работе через UI
        val result = core.solve("*")    // 0*
        assertEquals("Error", result)
    }

    @Test
    fun divisionInput() {
        // core.solve("") не подразумевается при нормальной работе через UI
        val result = core.solve("/")    // 0/
        assertEquals("Error", result)
    }

    @Test
    fun exponentialInput() {
        // core.solve("") не подразумевается при нормальной работе через UI
        val result = core.solve("^")    // 0^
        assertEquals("Error", result)
    }

    @Test
    fun zeroBeforeMinusAddition() {
        val result = core.solve("-2+2")
        assertEquals("0", result)
        verify(logger).d("inner tokenize result", "[0, -, 2, +, 2]")
    }

    // не будет работать если не убирать лишние токены, соответственно не вылетет ошибка и не будет Error
    @Test
    fun removeUselessTokens() {
        val result = core.solve("2+3-.")
        assertEquals("Error", result)
    }

    @Test
    fun processInputSimpleSequence() {
        var expression = ""
        expression = core.processInput(expression, "2")
        expression = core.processInput(expression, "+")
        expression = core.processInput(expression, "3")
        expression = core.processInput(expression, "*")
        expression = core.processInput(expression, "4")
        assertEquals("2+3*4", expression)
    }

    @Test
    fun processInputSequenceWithDot() {
        var expression = ""
        expression = core.processInput(expression, "2")
        expression = core.processInput(expression, ".")
        expression = core.processInput(expression, "3")
        expression = core.processInput(expression, "*")
        expression = core.processInput(expression, "4")
        assertEquals("2.3*4", expression)
    }

    @Test
    fun processInputDotRemove() {
        var expression = ""
        expression = core.processInput(expression, "7")
        expression = core.processInput(expression, ".")
        expression = core.processInput(expression, "-")
        expression = core.processInput(expression, ")")
        assertEquals("7.-)", expression)
    }

    @Test
    fun processInputAutoZeroDotSequence() {
        var expression = ""
        expression = core.processInput(expression, "2")
        expression = core.processInput(expression, "-")
        expression = core.processInput(expression, ")")
        expression = core.processInput(expression, ".")
        expression = core.processInput(expression, "5")
        assertEquals("2-)0.5", expression)
    }

    @Test
    fun multipleDotsInOneDigitAttempt() {
        var expression = ""
        expression = core.processInput(expression, "7")
        expression = core.processInput(expression, ".")
        expression = core.processInput(expression, "7")
        expression = core.processInput(expression, ".")
        expression = core.processInput(expression, "7")
        assertEquals("7.77", expression)
    }

    @Test
    fun operatorsSwap() {
        var expression = "1"
        expression = core.processInput(expression, "+")
        expression = core.processInput(expression, "-")
        expression = core.processInput(expression, "*")
        expression = core.processInput(expression, "/")
        expression = core.processInput(expression, "^")
        assertEquals("1^", expression)
    }

    @Test
    fun exponentialIntoMinusWithAutoOpenParenthesis() {
        var expression = "1"
        expression = core.processInput(expression, "^")
        expression = core.processInput(expression, "-")
        assertEquals("1^(-", expression)
    }

    @Test
    fun tokensListIsEmptyException() {
        val expr = "Infinit"
        try {
            core.solve(expr)
        } catch (e: NoSuchElementException) {
            assertEquals("Tokens list is empty", e.message)
        }
    }

    // Тест пройден если он провалится (поиск необработанных ошибок)
    @Test(expected = AssertionError::class)
    fun shouldFail() {
        val expr = "Infinite"
        assertThrows(NoSuchElementException::class.java) {
            core.solve(expr)
        }
    }

    @Test
    fun inputErrorInvalidDotParenthesis() {
        val invalidInputs = listOf("2+3.)", "7.(5", "2+(.3", "4+).2") // Example invalid inputs

        val result = invalidInputs.map { core.solve(it) }
        assertEquals(List(result.size) { "Error" }, result)
    }

    @Test
    fun inputErrorNoNumbers() {
        val invalidInputs = listOf("+++", "()", ".-+") // Example inputs with no numbers

        val result = invalidInputs.map { core.solve(it) }
        assertEquals(List(result.size) { "Error" }, result)
    }

    @Test
    fun invalidExpressions() {
        val invalidInputs = listOf("2+3-.", "-7.*.*.*", "7.)-6", "7-.))", "7.)", "2+3.)", "7.(5", "4+).2", "+++","()", ".-+", "5.)", ").5", "5.(", "2+(.3", "(.5", "(4-5)-.)")

        val result = invalidInputs.map { core.solve(it) }
        assertEquals(List(result.size) { "Error" }, result)
    }

    @Test
    fun validExpressions() {
        val validInputs = listOf("3*", "3+", "55-", "55-5-", "2+3", "(4*5)-2", "10/2.5", "7)")
        val expectedValidResults = listOf("3", "3", "55", "50", "5", "18", "4", "7")

        val result = validInputs.map { core.solve(it) }
        assertEquals(expectedValidResults, result)
    }
}