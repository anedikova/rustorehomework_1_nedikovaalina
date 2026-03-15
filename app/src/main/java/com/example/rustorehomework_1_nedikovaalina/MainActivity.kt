package com.example.rustorehomework_1_nedikovaalina

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
import com.example.rustorehomework_1_nedikovaalina.ui.theme.RuStoreHomework_1_NedikovaAlinaTheme
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    companion object {
        const val EXTRA_TEXT = "extra_text"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RuStoreHomework_1_NedikovaAlinaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IntentsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IntentsScreen(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    var input by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Введите текст / номер") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // 1) ЯВНЫЙ INTENT: открыть SecondActivity и передать строку
        Button(
            onClick = {
                val text = input.trim()
                if (text.isEmpty()) {
                    Toast.makeText(ctx, "Введите текст", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val intent = Intent(ctx, SecondActivity::class.java).apply {
                    putExtra(MainActivity.EXTRA_TEXT, text)
                }
                ctx.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Открыть вторую Activity") }

        Spacer(Modifier.height(8.dp))

        // 2) НЕЯВНЫЙ INTENT: ACTION_DIAL (звонилка) с номером из поля
        Button(
            onClick = {
                val phoneRaw = input.trim()
                if (phoneRaw.isEmpty()) {
                    Toast.makeText(ctx, "Введите номер телефона", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                // простая валидация: оставим цифры и +
                val phone = phoneRaw.replace(Regex("[^0-9+]"), "")
                if (phone.isEmpty()) {
                    Toast.makeText(ctx, "Номер некорректный", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }

                if (dialIntent.resolveActivity(ctx.packageManager) != null) {
                    ctx.startActivity(dialIntent)
                } else {
                    Toast.makeText(ctx, "Нет приложения для звонков", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Позвонить другу") }

        Spacer(Modifier.height(8.dp))

        // 3) СИСТЕМНЫЙ INTENT: ACTION_SEND (поделиться текстом)
        Button(
            onClick = {
                val text = input.trim()
                if (text.isEmpty()) {
                    Toast.makeText(ctx, "Введите текст для отправки", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                }

                ctx.startActivity(Intent.createChooser(shareIntent, "Поделиться через…"))
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Поделиться текстом") }
    }
}

@Preview(showBackground = true)
@Composable
fun IntentsScreenPreview() {
    RuStoreHomework_1_NedikovaAlinaTheme {
        IntentsScreen()
    }
}