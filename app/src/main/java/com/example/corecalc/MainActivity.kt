package com.example.corecalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.corecalc.ui.theme.CoreCalcTheme

class MainActivity : ComponentActivity() {

    private val core = Core()
    /*private val getP1: () -> Double = {
        core.getP1()
    }
    private val setP1: (p1: Double) -> Unit = { p1 ->
        core.setP1(p1)
    }
    private val getP2: () -> Double = {
        core.getP2()
    }
    private val setP2: (p2: Double) -> Unit = { p2 ->
        core.setP2(p2)
    }
    private val getSign: () -> SIGN? = {
        core.getSign()
    }
    private val setSign: (sign: SIGN?) -> Unit = { sign ->
        core.setSign(sign)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoreCalcTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Composition(core, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
