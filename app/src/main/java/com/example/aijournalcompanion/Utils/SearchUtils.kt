package com.example.aijournalcompanion.Utils

import androidx.compose.runtime.key
import com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree.BinarySearchTree
import com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree.Node
import com.example.aijournalcompanion.DataStructs.DataState
import com.example.aijournalcompanion.CustomDataTypes.DoublyLinkedList.DoublyLinkedList
import com.example.aijournalcompanion.DataStructs.EmotionResponse
import com.example.aijournalcompanion.UI.searchChoices
data class searchContext(
    val type: searchChoices,
    val data: DataState
)
class SearchUtils {
    companion object {
        // Sanitises Input
        private fun transform( input: String): String{
            return input.trim().lowercase()
        }
        private fun search(input: String,ctx: searchContext): List<EmotionResponse> {
            // Converts input into emotion response object
            val key = EmotionResponse(emotion = input, advice = "", text = "")
            // Returns result of Specific search based upon enum of user choice
            return when (ctx.type) {
                searchChoices.BinaryTree -> {
                    // Calls Search of bin tree
                    ctx.data.tree.searchByEmotion(input)
                }
                // Searches hashmap and returns index of found item
                searchChoices.HashBasedMap -> {
                    val result: EmotionResponse? = ctx.data.hash[key]
                    if (result != null) listOf(result) else emptyList()
                }
                // Searches doubly linked list and returns index of found item
                searchChoices.DoublyLinkedList ->{
                     ctx.data.list.FoundEmotions(input)
                }
                // Returns Prompt for user to select a choice
                searchChoices.SelectSearchChoice -> {
                    emptyList()
                }
            }
        }
        // Formats results for frontend display
        private fun display(input: List<EmotionResponse>): List<EmotionResponse>{
            if (input.isNotEmpty()) {
                return input
            }
            val fail = mutableListOf<EmotionResponse>()
            val response = EmotionResponse(emotion = "Please Select a Search Option or load data", advice = "", text = "")
            fail.add(1, (response))
            return fail
        }
        // Functional PipeLine, Takes the first two combined out put and passes output to the next, which then combines with the next generic and produces a result
        private infix fun<A,B,C> (  (A)-> B).then(next : (B) -> C): (A) -> C = {input -> next(this(input))}

        fun pipe(input: String,context: searchContext): List<EmotionResponse>{
            // Passes a string down the pipe that is mutated each step
            val p = ::transform then {s -> search(s,context) } then ::display
            // returns function with input of first string
            return p(input)
        }
    }
}