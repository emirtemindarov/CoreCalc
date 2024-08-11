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

    private val INFINITY_SIGN: String = "∞"
    private val NOT_A_NUMBER_SIGN: String = "NaN"
    private val ERROR_SIGN: String = "Error"

    // либо назначить кнопку для вычисления, либо каждый раз вызывать при изменении выражения
    fun solve(expr: String): String {
        logger.d("expr", expr)

        // core.solve("") не подразумевается при нормальной работе через UI
        if (expr.isEmpty()) {
            return "0"
        }

        var result = .0

        try {
            result = evaluate(postfix(tokenize(expr)))
        } catch (e: Exception) {
            e.message?.let { logger.e("result error", it) }
            return ERROR_SIGN
        } finally {
            logger.d("result", "$result")
        }

        if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY) {
            return INFINITY_SIGN
        } else if (result.isNaN()) {
            return NOT_A_NUMBER_SIGN
        }

        return if (result == result.toInt().toDouble()) {
            result.toInt().toString() // Округляем до целого
        } else {
            result.toString() // Выводим как есть
        }
    }

    fun setAutoSolve(choice: Boolean) {
        when (choice) {
            true -> _autoSolve.value = true
            false -> _autoSolve.value = false
        }
    }

    // TODO убрать currentExpression.isNotEmpty()
    // TODO оптимизировать порядок в конце, проводя тесты при каждом изменении позиций
    fun processInput(currentExpression: String, inputChar: String): String {
        logger.d("currentExpression до", currentExpression)
        logger.d("inputChar", inputChar)
        val result = when {
            currentExpression == ERROR_SIGN -> inputChar
            currentExpression == NOT_A_NUMBER_SIGN -> inputChar
            currentExpression == INFINITY_SIGN -> inputChar
            currentExpression == "0" && inputChar in setOf("+", "*", "/", "^") -> currentExpression + inputChar
            // TODO надо ли?
            /*inputChar == ")" && currentExpression.count { it == '(' } <= currentExpression.count { it == ')' } ->
                // Игнорируем ввод закрывающей скобки, если нет открывающей
                currentExpression*/
            inputChar == "." && currentExpression.matches(Regex("\\d+")) ->
                currentExpression + inputChar
            inputChar == "." && currentExpression.last() == ')' ->
                currentExpression + "0." // Добавляем "0" перед точкой после закрывающей скобки
            inputChar == "." -> {
                val lastNumber = currentExpression.takeLastWhile { it.isDigit() || it == '.' }
                if (lastNumber.contains('.')) {
                    currentExpression // Ignore the input if the last number already has a decimal point
                } else {
                    currentExpression + inputChar
                }
            }
            inputChar in setOf("+", "*", "/", "^") && currentExpression.last().toString() in setOf("+", "-", "*", "/") ->
                currentExpression.dropLast(1) + inputChar // Заменяем последний знак на новый
            inputChar == "-" && currentExpression.last().toString() == "^" ->
                currentExpression + "(" + inputChar // Добавляем "(" после знака "^" если вводимый символ "-"
            inputChar in setOf("+", "-", "*", "/", "^") && currentExpression.last().toString() in setOf("+", "-", "*", "/", "^") ->
                currentExpression.dropLast(1) + inputChar // Игнорируем повторный ввод того же оператора
            currentExpression == "0" -> inputChar
            else -> currentExpression + inputChar
        }
        if (_autoSolve.value) {
            _earlyResult.value = result
        }
        logger.d("currentExpression после", result)
        return result
    }

    // Эта функция также ответственна за очистку ненужных конструкций в выражении
    private fun tokenize(expr: String): List<String> {

        /*if (expr.contains(Regex("""(?<!\d),(?!\d)|,(?=\()|(?<=\)),"""))) {
            throw ExpressionException("Invalid expression: misplaced comma in $expr")
        }

        if (expr.contains(Regex("""\.\(|\)\.|\.\)|\(\."""))) {
            throw ExpressionException("Invalid expression: invalid construction in $expr")
        }

        if (expr.any { it.isDigit() }) {
            throw ExpressionException("Invalid expression: no numbers found in $expr")
        }*/

        val invalidPattern = Regex("""(?<!\d)\.|\.(?!\d)|\.\(|\)\.|\.\)""")
        if (expr.contains(invalidPattern) || !expr.any { it.isDigit() }) {
            throw ExpressionException("Incorrect input in $expr")
        }

        // TODO здесь приоритетнее редактировать выражение если возможно
        val cleanExpr = expr
            .replace("()", "")
            .replace(")(", ")*(")

            // ")0.5" -> ")*0.5"
            .replace(Regex("""\)(\d)"""), ")*$1")
            // "0.5(" -> "0.5*("
            .replace(Regex("""(\d)\("""), "$1*(")

        logger.d("clean expr", cleanExpr)

        // TODO будут токены "log" ...
        val pattern = """(\d+\.\d+|\d+\.|\.\d+|\d+|\+|-|\*|/|\(|\)|\^)""".toRegex()
        val tokens = pattern.findAll(cleanExpr)
            .map { it.value }
            .toMutableList()

        logger.d("tokens", "$tokens")

        val result = mutableListOf<String>()

        // Убираем ненужные символы в начале. Это предотвращает появлении ошибки и не выбивает ERROR
        /*while (tokens.isNotEmpty() && tokens.first() == ")") {
            tokens.removeFirst()
        }*/
        // Добавляем "0" перед знаком "-" если он в начале
        while (tokens.isNotEmpty() && tokens.first() == "-") {
            tokens.add(0, "0")
        }
        // Убираем ненужные символы в конце.
        while (tokens.isNotEmpty() && tokens.last() in setOf("+", "-", "*", "/", "^")) {
            tokens.removeLast()
        }

        logger.d("inner tokenize result", "$tokens")

        // TODO манипуляции в виде expr.replace("(-", "(0-") могут заменить этот блок
        // Обработка строки без лишних конструкций
        for (i in tokens.indices) {
            if (i < tokens.size - 1 && tokens[i] == "(" && tokens[i + 1] in setOf("+", "-", "*", "/", "^")) {
                // Унарный минус после открывающей скобки
                result.add("(")
                result.add("0") // Добавляем "0" после открывающей скобки
            } else if (i > 0 && tokens[i] == "(" && tokens[i - 1].matches("""\d+""".toRegex())) {
                // Number followed by opening parenthesis
                result.add("*")
                result.add(tokens[i])
            } else if (i < tokens.size - 1 && tokens[i] == ")" && tokens[i + 1].matches("""\d+""".toRegex())) {
                // Closing parenthesis followed by number
                result.add(tokens[i])
                result.add("*")
            } else {
                result.add(tokens[i])
            }
        }

        // FIXME не видел никогда сообщения
        if (result.isNotEmpty()) {
            return result
        } else {
            throw NoSuchElementException("Tokens list is empty")
        }
    }

    private fun postfix(infix: List<String>): List<String> {
        logger.d("infix", "$infix")
        val output = mutableListOf<String>()
        val operators = mutableListOf<String>()
        for (token in infix) {
            when (token) {
                "^" -> { // Highest precedence
                    while (operators.isNotEmpty() && operators.last() == "^") {
                        output.add(operators.removeLast())
                    }
                    operators.add(token)
                }
                "*", "/" -> { // Medium precedence
                    while (operators.isNotEmpty() && operators.last() in setOf("*", "/", "^")) {
                        output.add(operators.removeLast())
                    }
                    operators.add(token)
                }
                "+", "-" -> { // Lowest precedence
                    while (operators.isNotEmpty() && operators.last() !in setOf("(", ")")) {
                        output.add(operators.removeLast())
                    }
                    operators.add(token)
                }
                "(" -> operators.add(token)
                ")" -> {
                    while (operators.isNotEmpty() && operators.last() != "(") {
                        output.add(operators.removeLast())
                    }
                    if (operators.isNotEmpty() && operators.last() == "(") {
                        operators.removeLast()
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
        logger.d("expr перед clear", expr)

        return when (expr) {
            ERROR_SIGN, NOT_A_NUMBER_SIGN, INFINITY_SIGN -> {
                logger.d("$expr clear", expr)
                "0"
            }

            else -> {
                if (expr.isNotEmpty()) {
                    val clearedExpr = expr.dropLast(1)
                    logger.d("clearedExpr", clearedExpr)
                    clearedExpr.ifEmpty {
                        logger.d("нет символов после clear", clearedExpr)
                        return@clear "0" // FIXME почему без return не работает
                    }
                    clearedExpr // Return clearedExpr even if it's empty
                } else { // Не выполнится при работе через UI
                    logger.d("else clear при выражении", expr)
                    "0"
                }
            }
        }
    }

    fun allClear() : String {
        return "0"
    }
}

class ExpressionException(message: String) : Exception(message)