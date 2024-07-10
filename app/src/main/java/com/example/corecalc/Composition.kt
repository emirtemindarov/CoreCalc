package com.example.corecalc

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoundedButton(
    text: @Composable () -> Unit,
    buttonColor: Color = Color.Unspecified,
    action: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(buttonColor)
            .clickable {
                Log.i("click", text.toString())
                action()
            },
        contentAlignment = Alignment.Center
    ) {
        text()
    }
}

@Composable
fun Composition(
    core: Core/*(autoSolve = true)*/ /*= viewModel()*/,
    modifier: Modifier
) {
    val expr = remember { mutableStateOf("0") }
    // val earLyResult = core.earlyResult.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Выражение
        Row(
            modifier = Modifier
                .background(Color.White)
                .weight(0.5f)
                .fillMaxSize()
                .padding(25.dp, 50.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = expr.value,
                color = Color.Black,
                fontSize = (60-(expr.value.length)).sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold
                //modifier = Modifier
                //    .background(Color.LightGray)
            )
        }

        // Визуальный эффект возвышения для панели
        Surface(modifier = Modifier
            .wrapContentSize()
            .weight(0.5f),
            tonalElevation = 8.dp,
            shadowElevation = 20.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            // Панель кнопок
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                    // 1 ряд кнопок
                    Row(
                        modifier = Modifier
                            //.background(Color.Gray)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundedButton({
                            Text("1",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "1")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("2",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "2")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("3",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "3")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("+",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "+")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("=",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.solve(expr.value)
                            }
                        )
                    }
                    // Ряд разделитель
                    Row(
                        modifier = Modifier
                            //.background(Color.Gray)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                    }
                    // 2 ряд кнопок
                    Row(
                        modifier = Modifier
                            //.background(Color.Gray)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundedButton({
                            Text("4",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "4")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("5",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "5")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("6",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "6")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("-",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "-")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("C",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.clear(expr.value)
                            }
                        )
                    }
                    // Ряд разделитель
                    Row(
                        modifier = Modifier
                            //.background(Color.Gray)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                    }
                    // 3 ряд кнопок
                    Row(
                        modifier = Modifier
                            //.background(Color.Gray)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundedButton({
                            Text("7",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "7")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("8",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "8")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("9",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "9")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("*",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "*")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("AC",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = "0"
                            }
                        )
                    }
                    // Ряд разделитель
                    Row(
                        modifier = Modifier
                            //.background(Color.Gray)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                    }
                    // 4 ряд кнопок
                    Row(
                        modifier = Modifier
                            //.background(Color.Gray)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundedButton({
                            Text("0",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "0")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text(".",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, ".")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("%",   // Процент у второго числа от первого (p1 % p2 = 100 % 50 = 50 )
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                // TODO либо посчитать выражение и поделить на 100, либо поделить на 100 только токен на котором курсор

                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("/",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "/")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("R",  // Вставка результата последнего вычисления?    дополнительно размещать историю вычислений поверх текущего поля и можно будет кликнуть и чтото произойдет?
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                // TODO где хранить это? в переменной класса?

                            }
                        )
                    }
                    // Ряд разделитель
                    Row(
                        modifier = Modifier
                            //.background(Color.Gray)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(64.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                    }
                    // 5 ряд кнопок
                    Row(
                        modifier = Modifier
                            //.background(Color.Gray)
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundedButton({
                            Text("(",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "(")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text(")",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, ")")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("^",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                expr.value = core.processInput(expr.value, "^")
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("lg",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                        },
                            action = {
                                // TODO log10(b) = lg(b)   2^3 = 8 => log2(8) = 3
                            }
                        )
                        Surface(modifier = Modifier
                            .wrapContentSize(),
                            shadowElevation = 1.dp
                        ) {
                            VerticalDivider(
                                modifier = Modifier.height(54.dp),
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                        RoundedButton({
                            Text("e",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.SansSerif)
                            },
                            action = {
                                // TODO core.processInput(expr.value, "e") добавляется на место курсора

                            }
                        )
                        // TODO
                        // div(, mod(, log2(, abs, sqrt, buf - открыть список результатов для вставки?,
                        // log2(x) = ln(x) / ln(2)

                        // TODO core.processInput(expr.value, "√") добавляется на место курсора      sqrt
                        // TODO  core.processInput(expr.value, "abs(") добавляется на место курсора  abs
                        // TODO loge(b) = ln(b)   2^3 = 8 => log2(8) = 3                             ln
                    }
                }


            }
        }
    }
}