package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Включаем полноэкранный режим, но дальше Scaffold всё компенсирует
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                // Используем Scaffold, чтобы он высчитал отступы от камеры
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Передаем эти безопасные отступы (innerPadding) в наше приложение
                    TipCalculatorApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TipCalculatorApp(modifier: Modifier = Modifier) {
    var orderAmount by remember { mutableStateOf("") }
    var dishesCount by remember { mutableStateOf("") }
    var tipPercentage by remember { mutableFloatStateOf(0f) }

    // ЛОГИКА: Вычисляем текущую скидку
    val count = dishesCount.toIntOrNull() ?: 0
    val activeDiscount = when {
        count in 1..2 -> 3
        count in 3..5 -> 5
        count in 6..10 -> 7
        count > 10 -> 10
        else -> 0
    }

    // Применяем отступы сверху (от камеры) + наши 16.dp по краям
    Column(modifier = modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            Text("Сумма заказа:", modifier = Modifier.width(140.dp), fontSize = 16.sp)
            OutlinedTextField(
                value = orderAmount, onValueChange = { orderAmount = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth()
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
            Text("Количество блюд:", modifier = Modifier.width(140.dp), fontSize = 16.sp)
            OutlinedTextField(
                value = dishesCount, onValueChange = { dishesCount = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth()
            )
        }

        Text("Чаевые:", fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Text("0")
            Slider(value = tipPercentage, onValueChange = { tipPercentage = it }, valueRange = 0f..25f, modifier = Modifier.weight(1f).padding(horizontal = 8.dp))
            Text("25")
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Скидка:", fontSize = 16.sp, modifier = Modifier.padding(end = 16.dp))

            val discounts = listOf(3, 5, 7, 10)
            discounts.forEach { percent ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(end = 8.dp)) {
                    RadioButton(
                        selected = (activeDiscount == percent),
                        onClick = null
                    )
                    Text("$percent%")
                }
            }
        }
    }
}