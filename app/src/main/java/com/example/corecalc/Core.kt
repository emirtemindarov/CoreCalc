package com.example.corecalc

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.pow

class Core(
    autoSolve: Boolean = false,
    private val logger: LoggerInterface = LogWrapper()
) : ViewModel() {
    private val _autoSolve: MutableStateFlow<Boolean> = MutableStateFlow(autoSolve)
    // val autoSolve: StateFlow<Boolean> = _autoSolve.asStateFlow()
    private val _earlyResult: MutableStateFlow<String> = MutableStateFlow("")
    val earlyResult: StateFlow<String> = _earlyResult.asStateFlow()

    // либо назначить кнопку для вычисления, либо каждый раз вызывать при изменении выражения
    fun solve(expr: String): String {
        logger.d("expr", expr)
        var result = .0

        try {
            result = evaluate(postfix(tokenize(expr)))
        } catch (e: Exception) {
            e.message?.let { logger.e("result error", it) }
        } finally {
            logger.d("result", "$result")
        }

        return try {
            logger.d("result.toInt().toString()", "${result.toInt()}")
            result.toInt().toString()
        } catch (e: Exception) {
            result.toString()
        }
    }

    fun setAutoSolve(choice: Boolean) {
        when (choice) {
            true -> _autoSolve.value = true
            false -> _autoSolve.value = false
        }
    }

    fun processInput(currentExpression: String, inputChar: String): String {
        val result = when {
            currentExpression == "0" && inputChar in setOf("+", "*", "/", "^") -> currentExpression + inputChar
            currentExpression == "0" -> inputChar
            inputChar in setOf("+","-", "*", "/", "^") &&
                    currentExpression.isNotEmpty() &&
                    currentExpression.last().toString() in setOf("+", "-", "*", "/", "^") ->
                currentExpression.dropLast(1) + inputChar
            else -> currentExpression + inputChar
        }
        if (_autoSolve.value) {
            _earlyResult.value = result
        }
        return result
    }

    private fun tokenize(expr: String): List<String> {
        val pattern = """(\d+\.\d+|\d+\.|\.\d+|\d+|\+|-|\*|/|\(|\)|\^)""".toRegex()
        val tokens = pattern.findAll(expr)
            .map { it.value }
            .toMutableList()

        if (tokens.isNotEmpty() && tokens[0] == "-") {
            tokens.add(0, "0")
        }

        // ")(" = ")", "*", "("
        val result = mutableListOf<String>()
        for (i in tokens.indices) {
            if (i < tokens.size - 1 && tokens[i] == ")" && tokens[i + 1] == "(") {
                result.add(")")
                result.add("*")
                result.add("(")
            } else if (i > 0 && tokens[i] == "(" && tokens[i - 1].matches("""\d+""".toRegex())) {
                // Number followed by opening parenthesis
                result.add("*")
                result.add(tokens[i])
            } else if (i < tokens.size - 1 && tokens[i] == ")" && tokens[i+ 1].matches("""\d+""".toRegex())) {
                // Closing parenthesis followed by number
                result.add(tokens[i])
                result.add("*")
            } else {
                result.add(tokens[i])
            }
        }

        // Handle trailing operators
        if (result.isNotEmpty()) {
            val lastToken = result.last()
            if (lastToken == "+" || lastToken == "-") {
                result.add("0")
            } else if (lastToken == "*" || lastToken == "/" || lastToken == "^") {
                result.add("1")
            }
        }

        return result
    }

    private fun postfix(infix: List<String>): List<String> {
        logger.d("infix", "$infix")
        val output = mutableListOf<String>()
        val operators = mutableListOf<String>()
        for (token in infix) {
            when (token) {
                "+", "-" -> {
                    while (operators.isNotEmpty() && operators.last() != "(") {
                        output.add(operators.removeLast())
                    }
                    operators.add(token)
                }
                "*", "/", "^" -> {
                    while (operators.isNotEmpty() && operators.last() != "("
                        && (operators.last() == "*" || operators.last() == "/" || operators.last() == "^")) {
                        output.add(operators.removeLast())
                    }
                    operators.add(token)
                }
                "(" -> operators.add(token)
                ")" -> {
                    while (operators.isNotEmpty() && operators.last() != "(") {
                        output.add(operators.removeLast())
                    }
                }
                else -> output.add(token)
            }
            logger.d("operators", "$operators")
        }
        while (operators.isNotEmpty()) {
            when (operators.last()) {
                "(" -> operators.removeLast()
                else -> output.add(operators.removeLast())
            }
        }
        return output
    }

    private fun evaluate(postfix: List<String>): Double {
        logger.d("postfix", "$postfix")
        val stack = mutableListOf<Double>()
        for (token in postfix) {
            if (token == "+" || token == "-" || token == "*" || token == "/" || token == "^") {
                val right = stack.removeLast()
                val left = stack.removeLast()
                logger.d("right", "$right")
                logger.d("left", "$left")
                when (token) {
                    "+" -> stack.add(left + right)
                    "-" -> stack.add(left - right)
                    "*" -> stack.add(left * right)
                    "/" -> stack.add(left / right)
                    "^" -> stack.add(left.pow(right))
                }
            } else {
                stack.add(token.toDouble())
            }
        }
        return stack.last()
    }

    fun clear(expr: String): String {
        return if (expr.isNotEmpty()) {
            val clearedExpr = expr.dropLast(1)
            clearedExpr.ifEmpty { "0" }
        } else {
            "0"
        }
    }
}