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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.aijournalcompanion.PIPELINES.Context
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class ComposeFunctions {
    @Composable
    fun ViewBox(
        list: List<EmotionResponse>,
        context: Context)
    {
        var draggedItemIndex by remember { mutableStateOf<Int?>(null) }
        var isOverTrash by remember { mutableStateOf(false) }
        var trashBounds by remember { mutableStateOf(Rect.Zero) }
        var itemPositions by remember { mutableStateOf(mapOf<Int, Offset>()) }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.height(300.dp)
            ) {

                itemsIndexed(list) { index, item ->

                    var offset by remember { mutableStateOf(Offset.Zero) }

                    Box(

                        modifier = Modifier
                            .onGloballyPositioned {
                                itemPositions = itemPositions + (index to it.positionInRoot())
                            }
                            .fillMaxWidth()
                            .padding(8.dp)
                            .offset {
                                IntOffset(
                                    offset.x.roundToInt(),
                                    offset.y.roundToInt()
                                )
                            }
                            .background(
                                if (draggedItemIndex == index)
                                    Color.Gray
                                else
                                    Color.LightGray
                            )
                            .pointerInput(Unit) {

                                detectDragGestures(
                                    onDragStart = {
                                        draggedItemIndex = index
                                    },

                                    onDrag = { change, dragAmount ->

                                        change.consume()

                                        offset += dragAmount

                                        val startPos = itemPositions[index] ?: Offset.Zero
                                        val draggedCenter = startPos + offset

                                        isOverTrash = trashBounds.contains(draggedCenter)
                                    },

                                    onDragEnd = {

                                        if (draggedItemIndex == index && isOverTrash) {
                                            context.deleteItem(index)
                                        }

                                        draggedItemIndex = null
                                        isOverTrash = false

                                        offset = Offset.Zero
                                    },

                                    onDragCancel = {
                                        draggedItemIndex = null
                                        isOverTrash = false

                                        offset = Offset.Zero
                                    }
                                )
                            }
                    ) {

                        Text(
                            text = item.text,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .onGloballyPositioned {
                        trashBounds = it.boundsInRoot()
                    },
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = if (isOverTrash) Color.Red else Color.Gray,
                    modifier = Modifier.size(30.dp)
                )
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
    fun EmotionChartPopup(data: List<EmotionResponse>, onDismiss: () -> Unit){
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
    fun EmotionPieChart(data: List<EmotionResponse>) {
        val grouped = data.filterNot { it.emotion == "UNKNOWN" }. groupingBy { it.emotion }.eachCount()
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
                        loadUrl("file:///android_asset/help.html")
                    }
                })
            }
        }
    }
}