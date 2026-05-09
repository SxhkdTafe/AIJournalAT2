package com.example.aijournalcompanion.PIPELINES

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.aijournalcompanion.DataState
import com.example.aijournalcompanion.SearchUtils
import com.example.aijournalcompanion.SortUtils
import com.example.aijournalcompanion.UI
import com.example.aijournalcompanion.EmotionResponse

class Context(
    var input: String = "",
    ){
    var searchSelected by mutableStateOf(UI.searchChoices.DEFAULT)
    var sortSelected by mutableStateOf(UI.sortChoices.DEFAULT)
    var journalInput by mutableStateOf("")
    var searchInput by mutableStateOf("")
    var result by mutableStateOf("")
    var data by mutableStateOf(DataState.Companion.from(emptyList()))
    var showHelp by mutableStateOf(false)
    var showChart by mutableStateOf(false)
    var emotions = mutableListOf<String>()
    enum class InputField {
        Journal,
        Search
    }
    fun activeVal (v: InputField): String{
        return when(v){
            InputField.Journal -> journalInput
            InputField.Search -> searchInput
        }
    }
}
class PipelineBuilder {
    val steps = mutableListOf<suspend Context.() -> Unit>()
    fun consumeInputUI(v: Context.InputField){
        steps +={
            input = activeVal(v)
        }
    }
    fun backEndToFront(pipeline: suspend (String) -> EmotionResponse) {
        steps +={
            val res = pipeline(input)
            result = res.text
            emotions.add(res.emotion)
        }
    }
    fun updateExternalData( ){
        steps += {
            data = DataState.update(data,result)
        }
    }
    fun search(){
        steps +={
            result = SearchUtils.search(searchSelected,input,data)
        }
    }
    fun sort(){
        steps +={
            data = DataState.from(SortUtils.sort(sortSelected,data.items))
        }
    }
    fun displayHelp(){
        steps +={
            showHelp = true
        }
    }
    fun displayChart(){
        steps +={
            showChart = true
        }
    }
    suspend fun run(context: Context){
        for(step in steps){
            context.step()
        }
    }
}
