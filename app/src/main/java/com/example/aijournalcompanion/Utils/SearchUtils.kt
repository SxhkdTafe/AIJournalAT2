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

        private fun transform( input: String): String{
            return input.trim().lowercase()
        }
        private fun search(input: String,ctx: searchContext): String {
            val key = EmotionResponse(emotion = input, advice = "", text = "")
            return when (ctx.type) {
                searchChoices.BinaryTree -> {
                    val res = ctx.data.tree.searchByEmotion(input)
                    if ( res != null){
                        "${res.value.emotion} Found"
                    }
                    else{
                        "null: Not found"
                    }
                }
                searchChoices.HashBasedMap -> ctx.data.hash[key]?.toString() ?:"null: Not found"
                searchChoices.DoublyLinkedList ->{
                    val r = ctx.data.list.indexOfEmotion(input)
                    if(r == -1){
                        "null: Not found"
                    }
                    else{
                        r.toString()
                    }
                }
                searchChoices.SelectSearchChoice -> "Please Select a search type"
            }
        }
        private fun display(input: String): String{
            if (input == "Please Select a search type") {
                return input
            }
            return "This is where the first instance of the value you have searched is $input"
        }
        private infix fun<A,B,C> (  (A)-> B).then(next : (B) -> C): (A) -> C = {input -> next(this(input))}

        fun pipe(input: String,context: searchContext): String{
            val p = ::transform then {s -> search(s,context) } then ::display
            return p(input)
        }
    }
}