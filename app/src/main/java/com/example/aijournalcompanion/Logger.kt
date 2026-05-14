package com.example.aijournalcompanion

import com.example.aijournalcompanion.PipeLine.Context
import kotlinx.datetime.Clock

object Logger{
    // Takes in string defining what is logged and suspend Context object and returning a new defined step function
    fun logTaskMainPipe(name: String, step : suspend Context.() -> Unit): suspend Context.() -> Unit  = {
        val start = System.nanoTime()
        var elapsed: Double
        // Prints name and time it was executed
        println("$name | Time ${Clock.System.now()}")
        try {
            // Executes injected function
            this.step()
        }
        catch (e: Exception){
            // Prints Error and throws exception to program
            println("ERROR: $e")
            throw e
        }
        finally {
            // Prints time takes for operation
            elapsed = ((System.nanoTime() - start).toDouble()/1000000)
            println("Operation took $elapsed. ms")
        }
    }
}
