package com.example.aijournalcompanion

import com.example.aijournalcompanion.API.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class PipeLine {
    private val apiUrl = "http://10.0.2.2:8000/emotion_parse"
    private val call = Api()
    private fun trimInput(input: String) : String{
        return input.trim()
    }
    private fun formatResult(input: String) : String{
        return "Result: $input"
    }
    private fun callBackend(scope: CoroutineScope): (String) -> Deferred<String> = { input ->
        scope.async {
            call.sendToBackend(input, apiUrl)
        }
    }
    private infix  fun<A,B,C> ((A)-> B).then(next :(B) -> C): (A) -> C = {input -> next(this(input))}
    suspend fun runPipeline(input: String,scope: CoroutineScope): String{
        val syncPipe = ::trimInput then :: formatResult
        val cleaned = syncPipe(input)
        val deff = callBackend(scope)(cleaned)
        return  deff.await()
    }
}