package com.example.aijournalcompanion.PIPELINES

import com.example.aijournalcompanion.API.Api
import com.example.aijournalcompanion.EmotionResponse
class PipeLineAPI {
    private val apiUrl = "http://10.0.2.2:8000/emotion_parse"
    private val call = Api()
    private suspend fun trimInput(input: String) : String{
        return input.trim()
    }
    private suspend fun formatResult(input: EmotionResponse) : EmotionResponse{
        return EmotionResponse(
            emotion = input.emotion,
            advice = input.advice,
            text = "Result: ${input.text}")
    }
    private suspend fun callBackend(input: String): EmotionResponse {
        val json = call.sendToBackend(input, apiUrl)
        val emotion = json.get("Emotion")?.asString ?: "UNKNOWN"
        val advice = json.get("Advice")?.asString ?: ""
        if (json.has("error")){
            return EmotionResponse(
                emotion = "ERROR",
                advice = json.get("error").asString,
                text = "Backend error"
            )
        }
        return EmotionResponse(
            emotion = emotion,
            advice = advice,
            text = "Emotion: $emotion | Advice: $advice")
    }
    private infix fun<A,B,C> ( suspend (A)-> B).then(next : suspend (B) -> C):suspend (A) -> C = {input -> next(this(input))}
    suspend fun runPipeline(input: String): EmotionResponse{
        val asyncPipe = ::trimInput then :: callBackend then :: formatResult
        return  asyncPipe(input)
    }
}