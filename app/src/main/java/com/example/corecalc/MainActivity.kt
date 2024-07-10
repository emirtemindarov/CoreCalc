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
