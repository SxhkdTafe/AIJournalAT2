package com.example.aijournalcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aijournalcompanion.PIPELINES.Builder
import com.example.aijournalcompanion.PIPELINES.Context
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
    @Composable
    fun  MainScreen() {
        val elements = ComposeFunctions()
        val scope = rememberCoroutineScope()
        val context = remember(scope) {
            Context(
                input = ""
            )
        }
        val pipelines = Builder.build()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (context.showHelp){
                elements.LocalHtmlPopup {
                    context.showHelp = false
                }
            }
            if(context.showChart){
                elements.EmotionChartPopup (data =context.data.items, {context.showChart = false})
            }

            elements.ViewBox(context.data.items, context)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = context.result,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = context.journalInput,
                onValueChange = { context.journalInput = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {Text("Please enter the emotion you are feeling to get advice.")}
            )

            Spacer(modifier = Modifier.height(1.dp))
            TextField(
                value = context.searchInput,
                onValueChange = { context.searchInput = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,

                placeholder = {Text("Please enter the emotion you want to search")}
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    scope.launch {
                        pipelines.analyse.run(context)
                    }
                }) {
                    Text("Analyse")
                }
                Button(onClick = {
                    scope.launch {
                       pipelines.chart.run(context)
                    }
                }) {
                    Text("Show Chart")
                }
                Button(onClick = {
                    scope.launch {
                        pipelines.help.run(context)
                    }
                }) {
                    Text("Help")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    scope.launch {
                        pipelines.search.run(context)
                    }
                }) {
                    Text("Search")
                }
                Button(onClick = {
                    scope.launch {
                        pipelines.sort.run(context)
                    }
                }) {
                    Text("Sort")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                elements.DropdownMenuBox(
                    options = UI.searchChoices.entries.filter { it != UI.searchChoices.SelectSearchChoice } .map {it.name}.toTypedArray(),
                    selected = context.searchSelected.name,
                    onSelectedChange = {selectedName -> context.searchSelected = UI.searchChoices.valueOf(selectedName)})
                elements.DropdownMenuBox(
                    options = UI.sortChoices.entries.filter { it != UI.sortChoices.SelectSortChoice } .map {it.name}.toTypedArray(),
                    selected = context.sortSelected.name,
                    onSelectedChange = {selectedName -> context.sortSelected = UI.sortChoices.valueOf(selectedName)})
            }
        }
    }
}