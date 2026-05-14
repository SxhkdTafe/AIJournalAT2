package com.example.aijournalcompanion.PipeLine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.aijournalcompanion.DataStructs.DataState
import com.example.aijournalcompanion.Utils.SearchUtils
import com.example.aijournalcompanion.Utils.SortUtils
import com.example.aijournalcompanion.UI
import com.example.aijournalcompanion.DataStructs.EmotionResponse
import com.example.aijournalcompanion.Utils.searchContext
import com.example.aijournalcompanion.Logger.logTaskMainPipe

// Global Mutable bag of all objects in UI
class Context(
    // User input String
    var input: String = "",
    ){
    // Drop Down Box Variables
    var searchSelected by mutableStateOf(UI.searchChoices.SelectSearchChoice)
    var sortSelected by mutableStateOf(UI.sortChoices.SelectSortChoice)
    // Text Field Variables
    var journalInput by mutableStateOf("")
    var searchInput by mutableStateOf("")
    // OutPut TextBox Variable
    var result by mutableStateOf("")
    // Instance Variable of DataState class containing dataTypes
    var data by mutableStateOf(DataState.from())
    // Bool Variables mutating if respective button clicked
    var showHelp by mutableStateOf(false)
    var showChart by mutableStateOf(false)
    // Instance of Class for accessing parsed json objects
    var lastResponse: EmotionResponse? = null
    // Delegate enum
    enum class InputField {
        Journal,
        Search
    }
    // Delegate function to decide input Field for pipeline
    fun activeVal (v: InputField): String{
        return when(v){
            InputField.Journal -> journalInput
            InputField.Search -> searchInput
        }
    }
    // Func for deleting item internally from ViewBox
    fun deleteItem(item: EmotionResponse) {
        data.delete(item)

        data = DataState.rebuild(
            data.tree.toList()
        )
    }
}
class PipelineBuilder {
    // Command Queue for pipeline
    val steps = mutableListOf<suspend Context.() -> Unit>()

    // Selects Input Consumed for output variable
    fun consumeInputUI(v: Context.InputField){
        steps += logTaskMainPipe("GET input field"){
            input = activeVal(v)
        }
    }
    // Calls and sends input to API pipeline for result
    fun backEndToFront(pipeline: suspend (String) -> EmotionResponse) {
        steps += logTaskMainPipe("Command Queue API PIPE"){
            val res = pipeline(input)
            result = res.text
            // Extracts Object to be manipulated in other funcs
            lastResponse = res
            data = DataState.rebuild(data.toList() + res)
        }
    }
    // Calls the Search pipe that mutates depending upon searchSelected
    fun search(){
        steps += logTaskMainPipe("Search") {
            val ctx = searchContext(
                type = searchSelected,
                data = data
            )
            result = SearchUtils.pipe(input, ctx)
        }
    }
    // Sorts Data of List
    fun sort(){
        steps += logTaskMainPipe("Sort") {
            data = DataState.rebuild(SortUtils.sort(sortSelected, searchSelected, data))
        }
    }
    // Flips the Bool to control if help popup shows
    fun displayHelp(){
        steps += logTaskMainPipe("Help Bool Flip"){
            showHelp = true
        }
    }
    // Flips the Bool to control if chart popup shows
    fun displayChart(){
        steps += logTaskMainPipe("Chart bool Flip"){
            showChart = true
        }
    }
    // Executes compiled steps in pipeline in order
    suspend fun run(context: Context){
        for(step in steps){
            context.step()
        }
    }
}
