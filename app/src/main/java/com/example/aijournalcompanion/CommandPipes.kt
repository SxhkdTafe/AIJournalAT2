package com.example.aijournalcompanion

import kotlinx.coroutines.CoroutineScope

data class Context(
    var input: String,
    var result: String = ""
)


class PipelineBuilder {
    val context = Context("")
    fun  PipelineBuilder.trim() {
        context.input = context.input.trim()
    }
    suspend fun Context.backEndToFront(pipeline: suspend (String) -> String) {
        result = pipeline(input)
    }
    fun PipelineBuilder.apply(){

    }
    fun pipeline(block: PipelineBuilder.() -> Unit): Context {
        val builder = PipelineBuilder()
        builder.block()
        return builder.context
    }
}
