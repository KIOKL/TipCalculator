package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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

    // Состояние: развернут чек или нет
    var isReceiptExpanded by remember { mutableStateOf(false) }

    // ЛОГИКА ВЫЧИСЛЕНИЙ:
    val count = dishesCount.toIntOrNull() ?: 0
    val activeDiscount = when {
        count in 1..2 -> 3
        count in 3..5 -> 5
        count in 6..10 -> 7
        count > 10 -> 10
        else -> 0
    }

    // Математика для чека
    val baseAmount = orderAmount.toFloatOrNull() ?: 0f
    val discountAmount = baseAmount * (activeDiscount / 100f)
    val tipAmount = baseAmount * (tipPercentage / 100f)
    val totalAmount = baseAmount - discountAmount + tipAmount

    Column(modifier = modifier.padding(16.dp)) {
        // --- Верхняя часть из задания ---
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

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
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

        // --- БОНУС: Детализированный чек ---

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) // Полоска-разделитель

        // Кликабельный заголовок "Итого"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isReceiptExpanded = !isReceiptExpanded } // По клику меняем состояние
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Итого к оплате (нажми):",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = String.format("%.2f ₽", totalAmount),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Плавное появление деталей чека
        AnimatedVisibility(visible = isReceiptExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp)
            ) {
                ReceiptRow("Сумма заказа:", String.format("%.2f ₽", baseAmount))
                ReceiptRow("Скидка ($activeDiscount%):", String.format("-%.2f ₽", discountAmount), Color.Red)
                ReceiptRow("Чаевые (${tipPercentage.roundToInt()}%):", String.format("+%.2f ₽", tipAmount), Color(0xFF4CAF50)) // Зеленый цвет
            }
        }
    }
}

// Вспомогательная функция для отрисовки строк чека
@Composable
fun ReceiptRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray)
        Text(text = value, color = valueColor, fontWeight = FontWeight.Medium)
    }
}