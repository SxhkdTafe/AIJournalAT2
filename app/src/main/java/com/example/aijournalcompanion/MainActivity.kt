package com.example.aijournalcompanion

import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Color
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlin.collections.listOf

class MainActivity : ComponentActivity() {


    private val analysePipe = PipeLine()
    private var searchChoices = arrayOf(
        "Binary Tree", "Hash-based(Map)", "Doubly Linked List"
    )
    private var sortChoices = arrayOf(
        "Bubble Sort", "Insertion Sort", "Selection Sort"
    )
    var tree = BinarySearchTree<String>()
    var hash: HashMap<String, Int> = HashMap()
    var doubleLinkedList = DoublyLinkedList<String>()



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
        var searched by remember { mutableStateOf(false)  }
        var sorted by remember { mutableStateOf(false)  }
        var help by remember { mutableStateOf(false) }
        var result by remember { mutableStateOf("") }
        var searchSelected by remember { mutableStateOf(searchChoices[0]) }
        var sortSelected by remember { mutableStateOf(sortChoices[0]) }


        @Composable
        fun LocalHtmlPopup(onDismiss: () -> Unit) {
            if (help) {
                Dialog(onDismissRequest = { help = false }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.9f)
                    ) {
                        AndroidView(factory = { context ->
                            WebView(context).apply {
                                webViewClient = WebViewClient()
                                settings.javaScriptEnabled = true
                                loadUrl("file:///android_asset/help.html")
                            }
                        })
                    }
                }
            }
        }
        if (help) {
            LocalHtmlPopup(onDismiss = { help = false })
        }
        LaunchedEffect(mainItems) {
            tree = insertBinTree(mainItems)

            hash = HashMap(
                mainItems.associateWith { it.length }
            )

            doubleLinkedList = DoublyLinkedList<String>().apply {
                mainItems.forEach { add(it) }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            viewBox(mainItems)

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


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val scope = rememberCoroutineScope()
                Button(onClick = {
                    scope.launch {
                        val input = inputText
                        result = analysePipe.runPipeline(input,scope)
                    }
                }) {
                    Text("Analyse")
                }

                Button(onClick = {  showChart = true }) {
                    Text("Show Chart")

                }

                Button(onClick = { help = true }) {
                    Text("Help")
                }
                if (showChart){
                    EmotionChartPopup (data =mainItems, onDismiss = {showChart = false})
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
                                result =
                                    (SearchUtils.binTreeSearch(tree, searchText.trim())).toString()
                            }

                            "Hash-based(Map)" -> {
                                result =
                                    SearchUtils.hashMapSearch(hash, searchText.trim()).toString()
                            }

                            "Doubly Linked List" -> {
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
                    if (!sorted) return@LaunchedEffect
                    sorted = false
                    val workingList = when (searchSelected) {
                        "Binary Tree" -> tree.toList()
                        "Doubly Linked List" -> doubleLinkedList.toList()
                        "Hash-based(Map)" -> hash.keys.toList()
                        else -> mainItems
                    }
                    mainItems = when (sortSelected) {
                        "Bubble Sort" -> SortUtils.bubbleSortStrings(workingList)
                        "Insertion Sort" -> SortUtils.insertionSortStrings(workingList)
                        "Selection Sort" -> SortUtils.selectionSortStrings(workingList)
                        else -> workingList
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
    fun viewBox(list: List<String>){
        val items = remember { mutableStateListOf(*list.toTypedArray())}
        var draggedItemIndex by remember { mutableStateOf<Int?>(null)}
        LaunchedEffect(list) {
            items.clear()
            items.addAll(list)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            itemsIndexed(items){ index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            if (draggedItemIndex == index) Color.Gray else Color.LightGray
                        )
                        .pointerInput(Unit){
                            detectDragGestures (
                                onDragStart = {
                                    draggedItemIndex = index
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()

                                },
                                onDragEnd = {
                                    if (draggedItemIndex == index){
                                        items.removeAt(draggedItemIndex!!)
                                        draggedItemIndex = null
                                    }
                                },
                                onDragCancel = {
                                    draggedItemIndex = null
                                }
                            )
                        }

                ){
                    Text(text = item)
                }

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
            Color.Red,
            Color.Blue,
            Color.Green,
            Color.Magenta,
            Color.Cyan,
            Color.Yellow,
            Color.Gray
        ).map { it.toArgb() }
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