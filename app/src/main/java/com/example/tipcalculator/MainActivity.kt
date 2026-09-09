package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
        setContent {
            TipCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TipCalculatorApp()
                }
            }
        }
    }
}

@Composable
fun TipCalculatorApp() {
    // Переменные для хранения того, что вводит пользователь
    var orderAmount by remember { mutableStateOf("") }
    var dishesCount by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // Поле: Сумма заказа
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Сумма заказа:", modifier = Modifier.width(140.dp), fontSize = 16.sp)
            OutlinedTextField(
                value = orderAmount,
                onValueChange = { orderAmount = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Поле: Количество блюд
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Количество блюд:", modifier = Modifier.width(140.dp), fontSize = 16.sp)
            OutlinedTextField(
                value = dishesCount,
                onValueChange = { dishesCount = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}