package com.example.aijournalcompanion

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.aijournalcompanion.DataStructs.DataState
import com.example.aijournalcompanion.DataStructs.EmotionResponse
import com.example.aijournalcompanion.PipeLine.Context
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class ComposeFunctions {

    @Composable
    fun ViewBox(
        state: DataState,
        onDelete: (EmotionResponse) -> Unit
    ) {
        // Flattens data structure
        val list = state.list.toList()

        LazyColumn(

            modifier = Modifier.height(300.dp)
        ) {
            // Defines items contained and unique key of each
            items(list,key = { it.id }) { item ->
                // Amount Item has been dragged on the y axis
                var offsetY by remember { mutableStateOf(0f) }
                // Amount an item needs to be dragged
                val threshold = 150f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .offset { IntOffset(0, offsetY.toInt()) }
                        // Changes color of dragged item if exceeded threshold
                        .background(
                            if (offsetY > threshold) Color.Red else Color.LightGray
                        )
                        .pointerInput(Unit) {

                            detectDragGestures(
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    offsetY += dragAmount.y
                                },

                                onDragEnd = {
                                    // Deletes item if Threshold exceeded
                                    if (offsetY > threshold) {
                                        onDelete(item)
                                    }
                                    // Resets item to start if not exceeded
                                    offsetY = 0f

                                },
                                // Resets item to start of y axis where it began
                                onDragCancel = {
                                    offsetY = 0f
                                }
                            )
                        }
                ) {
                    Text(
                        // Displays item in column
                        text = item.text,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        // TrashBox icon to indicate where an item needs to be dragged to
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
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
                // If button clicked show all options
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // Displays each option in the array as a separate item
                options.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            // Cancels dropdown and sends selected item to backend
                            onSelectedChange(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
    // Func to display piechart to front end and to handle dismiss req
    @Composable
    fun EmotionChartPopup(state: DataState, onDismiss: () -> Unit){
        Dialog(onDismissRequest = onDismiss){
            Surface(modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.6f)
                ,  shape = RoundedCornerShape(12.dp), tonalElevation = 8.dp) {
                EmotionPieChart(state)
            }
        }
    }
    @Composable
    fun EmotionPieChart(state: DataState) {
        // Flattens data into list
        val data = state.list.toList()
        // Filters and groups data by emotion and count of each
        val grouped = data.filterNot { it.emotion == "UNKNOWN" || it.emotion == "ERROR" }. groupingBy { it.emotion }.eachCount()
        // Transforms data for presentation on piechart
        val entries = grouped.map {(emotion, count) -> PieEntry(count.toFloat(), emotion) }
        // Available colors for display in piechart
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
                // Includes colors into dataset
                dataSet.colors = colors
                // Converts into a presentable form for piechart
                val pieData = PieData(dataSet)
                // Assigns data to chart
                chart.data = pieData
                chart.invalidate()
            }
        )
    }
    @Composable
    fun LocalHtmlPopup(onDismiss: () -> Unit) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            ) {
                AndroidView(factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        // Local Html file in project structure
                        loadUrl("file:///android_asset/help.html")
                    }
                })
            }
        }
    }
}