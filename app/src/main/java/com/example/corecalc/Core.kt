package com.example.corecalc

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Core : ViewModel() {
    private val _p1: MutableStateFlow<Double> = MutableStateFlow(.0)
    val p1: StateFlow<Double> = _p1.asStateFlow()
    private val _p2: MutableStateFlow<Double?> = MutableStateFlow(null)
    val p2: StateFlow<Double?> = _p2.asStateFlow()
    private val _sign: MutableStateFlow<SIGN?> = MutableStateFlow(null)
    val sign: StateFlow<SIGN?> = _sign.asStateFlow()

    fun setP1(p1: Double) {
        _p1.value = p1
    }

    /*fun getP1(): Double {
        return _p1.value
    }*/

    fun setP2(p2: Double?) {
        _p2.value = p2
    }

    /*fun getP2(): Double? {
        return _p2.value
    }*/

    fun setSign(sign: SIGN?) {
        _sign.value = sign
    }

    /*fun getSign(): SIGN? {
        return _sign.value
    }*/

    fun addDigit(digit: Double) {
        Log.d("addDigit до", "${_p1.value} ${_sign.value} ${_p2.value}")
        Log.d("addDigit", "$digit")
        // 0
        if (_p1.value == .0 && _sign.value == null) {
            Log.d("addDigit #1", "addDigit #1")
            _p1.value = digit
        }
        // (0 + 0) || (1 + 0)
        else if (_sign.value != null && (_p2.value == null || _p2.value == .0)) {
            Log.d("addDigit #2", "addDigit #2")
            _p2.value = digit
        }
        // (0 + 1) || (1 + 1)
        else if (_sign.value != null && _p2.value != .0 && _p2.value != null) {
            Log.d("addDigit #3", "addDigit #3")
            _p2.value = _p2.value?.times(10)
            _p2.value = _p2.value?.plus(digit)
        }
        // 1
        else if (_p1.value != .0 && _sign.value == null) {
            Log.d("addDigit #4", "addDigit #4")
            _p1.value = _p1.value.times(10)
            _p1.value = _p1.value.plus(digit)
        } else { Log.w("addDigit #0", "addDigit #0") }
        Log.d("addDigit после", "${_p1.value} ${_sign.value} ${_p2.value}")
    }

    fun clear() {
        Log.d("clear до", "${_p1.value} ${_sign.value} ${_p2.value}")
        // 0
        if (_p1.value == .0 && _sign.value == null) {
            Log.d("clear #1", "clear #1")
            // NOP
        }
        // (0 +) || (1 +)
        else if (_sign.value != null && _p2.value == null) {
            Log.d("clear #2", "clear #2")
            _sign.value = null
        }
        // 1
        else if (_sign.value == null) {
            Log.d("clear #3", "clear #3")
            _p1.value = try {
                // FIXME
                Log.i("p1", _p1.value.toString().substring(0, _p1.value.toString().length - 1))
                _p1.value.toString().substring(0, _p1.value.toString().length - 1).toDouble()
            } catch (_: Exception) {
                .0
            }
        }
        // (0 + 1) || (1 + 1)
        else if (_sign.value != null && _p2.value != .0 && _p2.value != null) {
            Log.d("clear #4", "clear #4")
            _p2.value = try {
                _p2.value.toString().substring(0, _p2.value.toString().length - 1).toDouble()
            } catch (_: Exception) {
                null
            }
        } else { Log.w("clear #0", "clear #0") }
        Log.d("clear после", "${_p1.value} ${_sign.value} ${_p2.value}")
    }

    fun clearAll() {
        Log.d("clearAll до", "${_p1.value} ${_sign.value} ${_p2.value}")
        _p1.value = .0
        _p2.value = null
        _sign.value = null
        Log.d("clearAll после", "${_p1.value} ${_sign.value} ${_p2.value}")
    }

    fun solve() {
        Log.d("solve до", "${_p1.value} ${_sign.value} ${_p2.value}")
        _p1.value = when (_sign.value) {
            SIGN.ADD -> when(_p2.value) {
                null -> _p1.value
                else -> _p1.value + _p2.value!!
            }
            SIGN.SUB -> when(_p2.value) {
                null -> _p1.value
                else -> _p1.value - _p2.value!!
            }
            SIGN.MUL -> when(_p2.value) {
                null -> _p1.value
                else -> _p1.value * _p2.value!!
            }
            SIGN.DIV -> when(_p2.value) {
                null -> _p1.value
                else -> _p1.value / _p2.value!!
            }
            null -> {
                _p1.value
            } // знак не поставлен, но нажато равно
        }
        _sign.value = null
        _p2.value = null
        Log.d("solve после", "${_p1.value} ${_sign.value} ${_p2.value}")
    }
}

enum class SIGN(val s: String) {
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV(":")
}