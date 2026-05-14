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
import com.example.aijournalcompanion.PipeLine.Builder
import com.example.aijournalcompanion.PipeLine.Context
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
        // Custom Compose Objects initialization
        val elements = ComposeFunctions()
        // Coroutine for entire proj
        val scope = rememberCoroutineScope()
        val context = remember(scope) {
            Context(
                input = ""
            )
        }
        // Makes pipes for button actions
        val pipelines = Builder.build()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (context.showHelp){
                // Displays Help Html page popup
                elements.LocalHtmlPopup {
                    context.showHelp = false
                }
            }
            if(context.showChart){
                // Displays pie chart of prev emotions in data
                elements.EmotionChartPopup (state = context.data, {context.showChart = false})
            }
            // List View of all api call items in memory
            elements.ViewBox(context.data, onDelete = {item -> context.deleteItem(item)})

            Spacer(modifier = Modifier.height(12.dp))
            // Read only Result Text box
            Text(
                text = context.result,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            // User emotion input field
            TextField(
                // Assigns input field to internal var of pipeLineConstruct Class
                value = context.journalInput,
                onValueChange = { context.journalInput = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                // Flavour Text
                placeholder = {Text("Please enter the emotion you are feeling to get advice.")}
            )

            Spacer(modifier = Modifier.height(1.dp))

            // User Search for emotion input field
            TextField(
                // Assigns input field to internal var of pipeLineConstruct Class
                value = context.searchInput,
                onValueChange = { context.searchInput = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                // Flavour Text
                placeholder = {Text("Please enter the emotion you want to search")}
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    scope.launch {
                        // Executes the preConfigured Analyze PipeLine
                        pipelines.analyse.run(context)
                    }
                }) {
                    Text("Analyse")
                }
                Button(onClick = {
                    scope.launch {
                        // Executes the preConfigured Chart PipeLine
                       pipelines.chart.run(context)
                    }
                }) {
                    Text("Show Chart")
                }
                Button(onClick = {
                    scope.launch {
                        // Executes the preConfigured Help PipeLine
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
                        // Executes the preConfigured Search PipeLine
                        pipelines.search.run(context)
                    }
                }) {
                    Text("Search")
                }
                Button(onClick = {
                    scope.launch {
                        // Executes the preConfigured Sort PipeLine
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
                // Search DropDownBox
                elements.DropdownMenuBox(
                    // Sets Displayed options to search Enum options
                    options = UI.searchChoices.entries.filter { it != UI.searchChoices.SelectSearchChoice } .map {it.name}.toTypedArray(),
                    // Assigns Selected Choice to internal var of pipeLineConstruct Class
                    selected = context.searchSelected.name,
                    onSelectedChange = {selectedName -> context.searchSelected = UI.searchChoices.valueOf(selectedName)})

                // Sort DropDownBox
                elements.DropdownMenuBox(
                    // Sets Displayed options to sort Enum options
                    options = UI.sortChoices.entries.filter { it != UI.sortChoices.SelectSortChoice } .map {it.name}.toTypedArray(),
                    // Assigns Selected Choice to internal var of pipeLineConstruct Class
                    selected = context.sortSelected.name,
                    onSelectedChange = {selectedName -> context.sortSelected = UI.sortChoices.valueOf(selectedName)})
            }
        }
    }
}