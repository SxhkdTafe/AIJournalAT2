package com.example.aijournalcompanion


import android.R
import android.graphics.Color
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.aijournalcompanion.BinarySearchTree.BinarySearchTree
import com.example.aijournalcompanion.DoublyLinkedList.DoublyLinkedList
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
    private var searchChoices = arrayOf(
        "Binary Tree", "Hash-based(Map)", "Doubly Linked List"
    )
    private var sortChoices = arrayOf(
        "Bubble Sort", "Insertion Sort", "Selection Sort"
    )
    var tree = BinarySearchTree<String>()
    var hash = HashMap<String, Int>()
    var doubleLinkedList = DoublyLinkedList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        var inputText by remember { mutableStateOf("Enter the problems you are feeling to get advice") }
        var showChart by remember { mutableStateOf(false) }
        var analysed by remember { mutableStateOf(false)  }
        var result by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


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
                text = result,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))


            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))


            val test = listOf("sad","happy","angry","happy")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { analysed = true }) {
                    Text("Analyse")
                }

                Button(onClick = {  showChart = true }) {
                    Text("Show Chart")

                }

                Button(onClick = { /* Help */ }) {
                    Text("Help")
                }
                if (showChart){
                    EmotionChartPopup (data = test, onDismiss = {showChart = false})
                }
                LaunchedEffect(analysed) {
                    if (analysed){
                        val input = inputText.trim()
                        result =  sendToBackend(input)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DropdownMenuBox("Search Select", searchChoices)
                DropdownMenuBox("Sort Select", sortChoices)
            }
        }
    }
    @Composable
    fun DropdownMenuBox(label: String, options: Array<String>) {
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
                listOf(options[0], options[1], options[2]).forEach {
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


    @Composable
    fun EmotionChartPopup(data: List<String>, onDismiss: () -> Unit){
        Dialog(onDismissRequest = onDismiss){
            Surface(modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.6f)
                ,  shape = RoundedCornerShape(12.dp), tonalElevation = 8.dp) {
                EmotionPieChart(data)
            }
        }
    }

    @Composable
    fun EmotionPieChart(data: List<String>) {

        val grouped = data.groupingBy { it }.eachCount()
        val entries = grouped.map {(emotion, count) -> PieEntry(count.toFloat(), emotion) }
        val colors = listOf(
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.MAGENTA,
            Color.CYAN,
            Color.YELLOW,
            Color.GRAY
        )
        AndroidView(factory = { context ->
            PieChart(context)},
            update = { chart ->

                val dataSet = PieDataSet(entries, "Emotions").apply {
                    valueTextSize = 16f
                    chart.setEntryLabelTextSize(14f)

                }
                dataSet.colors = colors
                val pieData = PieData(dataSet)

                chart.data = pieData
                chart.invalidate()
            }
        )
    }
}