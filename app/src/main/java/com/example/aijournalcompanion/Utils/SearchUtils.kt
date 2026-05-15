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
        private fun search(input: String,ctx: searchContext): String {
            // Converts input into emotion response object
            val key = EmotionResponse(emotion = input, advice = "", text = "")
            // Returns result of Specific search based upon enum of user choice
            return when (ctx.type) {
                searchChoices.BinaryTree -> {
                    // Calls Search of bin tree
                    val res = ctx.data.tree.searchByEmotion(input)
                    // Formats Output for frontend
                    if ( res != null){
                        "${res.value.emotion} Found"
                    }
                    else{
                        "null: Not found"
                    }
                }
                // Searches hashmap and returns index of found item
                searchChoices.HashBasedMap -> ctx.data.hash[key]?.toString() ?:"null: Not found"
                // Searches doubly linked list and returns index of found item
                searchChoices.DoublyLinkedList ->{
                    val r = ctx.data.list.indexOfEmotion(input)
                    // Not found result default from search func
                    if(r == -1){
                        "null: Not found"
                    }
                    else{
                        r.toString()
                    }
                }
                // Returns Prompt for user to select a choice
                searchChoices.SelectSearchChoice -> "Please Select a search type"
            }
        }
        // Formats results for frontend display
        private fun display(input: String): String{
            if (input == "Please Select a search type") {
                return input
            }
            return "This is where the first instance of the value you have searched is $input"
        }
        // Functional PipeLine, Takes the first two combined out put and passes output to the next, which then combines with the next generic and produces a result
        private infix fun<A,B,C> (  (A)-> B).then(next : (B) -> C): (A) -> C = {input -> next(this(input))}

        fun pipe(input: String,context: searchContext): String{
            // Passes a string down the pipe that is mutated each step
            val p = ::transform then {s -> search(s,context) } then ::display
            // returns function with input of first string
            return p(input)
        }
    }
}