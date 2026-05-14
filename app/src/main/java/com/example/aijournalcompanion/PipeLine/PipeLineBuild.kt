package com.example.aijournalcompanion.PipeLine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.aijournalcompanion.API.PipeLineAPI

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
                },
                search = PipelineBuilder().apply {
                    consumeInputUI(Context.InputField.Search)
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


