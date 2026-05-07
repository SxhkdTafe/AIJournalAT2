package com.example.aijournalcompanion


import android.R
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlin.collections.associateBy
import kotlin.collections.listOf

class MainActivity : ComponentActivity() {

    private val apiUrl = "http://10.0.2.2:8000/emotion_parse"
    private var searchChoices = arrayOf(
        "Binary Tree", "Hash-based(Map)", "Doubly Linked List"
    )
    private var sortChoices = arrayOf(
        "Bubble Sort", "Insertion Sort", "Selection Sort"
    )




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        var mainItems by remember {   mutableStateOf(listOf<String>())}
        var inputText by remember { mutableStateOf("Enter the problems you are feeling to get advice") }
        var searchText by remember { mutableStateOf("Enter something to search") }
        var showChart by remember { mutableStateOf(false) }
        var analysed by remember { mutableStateOf(false)  }
        var searched by remember { mutableStateOf(false)  }
        var sorted by remember { mutableStateOf(false)  }
        var result by remember { mutableStateOf("") }
        var searchSelected by remember { mutableStateOf(searchChoices[0]) }
        var sortSelected by remember { mutableStateOf(sortChoices[0]) }



        mainItems = mainItems + ""
        var tree = BinarySearchTree<String>()
        var hash: HashMap<String, Int> = HashMap()
        var doubleLinkedList = DoublyLinkedList<String>()
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
                items(mainItems) { mainItems->
                    Text(mainItems)
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
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
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
                        mainItems += "$input | $result"
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { searched = true }) {
                    Text("Search")
                }

                Button(onClick = { sorted = true }) {
                    Text("Sort")
                }
                LaunchedEffect(searched, ) {
                    if (searched) {
                        when (searchSelected) {
                            "Binary Tree" -> {
                                tree = insertBinTree(mainItems)
                                result =
                                    (SearchUtils.binTreeSearch(tree, searchText.trim())).toString()
                            }

                            "Hash-based(Map)" -> {
                                hash = HashMap(mainItems.associateWith { it.length })
                                result =
                                    SearchUtils.hashMapSearch(hash, searchText.trim()).toString()
                            }

                            "Doubly Linked List" -> {
                                mainItems.forEach { doubleLinkedList.add(it) }
                                result = SearchUtils.doubleLinkedListSearch(
                                    doubleLinkedList,
                                    searchText.trim()
                                ).toString()
                            }
                        }
                        if (result == "null") {
                            result = "Please enter a value or select an appropriate method"
                        }
                    }
                }
                LaunchedEffect(sorted) {
                    if (sorted) {
                        when (sortSelected) {
                            "Bubble Sort" -> {
                                SortUtils.bubbleSortStrings(mainItems)
                            }

                            "Insertion Sort" -> {

                            }

                            "Selection Sort" -> {

                            }
                        }
                        if (result == "null") {
                            result = "Please enter a value or select an appropriate method"
                        }
                    }

                    }

            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DropdownMenuBox( options = searchChoices, selected = searchSelected, onSelectedChange = {searchSelected = it})
                DropdownMenuBox( options = sortChoices, selected = sortSelected, onSelectedChange = {sortSelected= it})
            }
        }
    }
    @Composable
    fun DropdownMenuBox( options: Array<String>, selected: String, onSelectedChange: (String) -> Unit)  {
        var expanded by remember { mutableStateOf(false) }
        Box {
            Button(onClick = { expanded = true}) {
                Text(selected)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            onSelectedChange(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    private fun insertBinTree(items: List<String>) :  BinarySearchTree<String>{
        val i =  BinarySearchTree<String>()
        items.forEach {
            i.insert(value = it)
        }
        return i
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