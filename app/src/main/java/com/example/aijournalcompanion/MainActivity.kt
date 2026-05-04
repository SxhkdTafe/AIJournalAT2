package com.example.aijournalcompanion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URI

class MainActivity : ComponentActivity() {

    private val apiUrl = "http://10.0.2.2:8000/emotion_parse"
    // IMPORTANT: emulator fix (NOT localhost)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        var inputText by remember { mutableStateOf("Name") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // List (replaces ListView)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(10) { index ->
                    Text("Journal item $index")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "TextView",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Input field (EditText)
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Row 1: Analyse / Show / Help
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { /* Analyse */ }) {
                    Text("Analyse")
                }

                Button(onClick = { /* Show Chart */ }) {
                    Text("Show Chart")
                }

                Button(onClick = { /* Help */ }) {
                    Text("Help")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Row 2: Search / Sort
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { /* Search */ }) {
                    Text("Search")
                }

                Button(onClick = { /* Sort */ }) {
                    Text("Sort")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Spinners → DropdownMenus (Compose equivalent)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DropdownMenuBox("Search")
                DropdownMenuBox("Sort")
            }
        }
    }
    @Composable
    fun DropdownMenuBox(label: String) {
        var expanded by remember { mutableStateOf(false) }
        var selected by remember { mutableStateOf(label) }

        Box {
            Button(onClick = { expanded = true }) {
                Text(selected)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Option 1", "Option 2", "Option 3").forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            selected = it
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    // -------------------------
    // NETWORK CALL (FIXED)
    // -------------------------
    private suspend fun sendToBackend(text: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val gson = Gson()
                val uri = URI(apiUrl)
                val conn = uri.toURL().openConnection() as HttpURLConnection

                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                val json = gson.toJson(mapOf("text" to text))
                conn.outputStream.use {
                    it.write(json.toByteArray())
                }

                val response = conn.inputStream.bufferedReader().readText()
                val obj = gson.fromJson(response, Map::class.java)

                obj["Advice"]?.toString() ?: "(No response)"
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
    }

    // -------------------------
    // DROPDOWN COMPONENT
    // -------------------------
    @Composable
    fun DropdownSelector(
        label: String,
        options: List<String>,
        selected: String,
        onSelected: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column {
            Text(label)

            Button(onClick = { expanded = true }) {
                Text(selected)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    // -------------------------
    // PIE CHART (MPAndroidChart inside Compose)
    // -------------------------
    @Composable
    fun EmotionPieChart(data: List<String>) {

        AndroidView(factory = { context ->
            PieChart(context).apply {
                val entries = ArrayList<PieEntry>()

                val grouped = data.groupingBy { it }.eachCount()

                grouped.forEach { (emotion, count) ->
                    entries.add(PieEntry(count.toFloat(), emotion))
                }

                val dataSet = PieDataSet(entries, "Emotions")
                val pieData = PieData(dataSet)

                this.data = pieData
                this.invalidate()
            }
        })
    }
}