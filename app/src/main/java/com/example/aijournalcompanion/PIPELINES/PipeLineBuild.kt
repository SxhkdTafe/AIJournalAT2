package com.example.aijournalcompanion.PIPELINES

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data class PipeLine(
    val analyse: PipelineBuilder,
    val search: PipelineBuilder,
    val sort: PipelineBuilder ,
    val help: PipelineBuilder,
    val chart: PipelineBuilder
)

 object Builder{
    @Composable
    fun  build () : PipeLine {
        val api = remember { PipeLineAPI() }
        return remember {
            PipeLine(
                analyse = PipelineBuilder().apply {
                    consumeInputUI(Context.InputField.Journal)
                    backEndToFront { input ->
                        api.runPipeline(input)
                    }
                    updateExternalData()
                },
                search = PipelineBuilder().apply {
                    consumeInputUI(Context.InputField.Journal)
                    search()
                },
                sort = PipelineBuilder().apply {
                    sort()
                },
                help = PipelineBuilder().apply {
                    displayHelp()
                },
                chart = PipelineBuilder().apply {
                    displayChart()
                }
            )

        }
    }
}


