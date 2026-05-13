package com.example.aijournalcompanion

import com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree.BinarySearchTree
import com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree.Node
import com.example.aijournalcompanion.CustomDataTypes.DataState
import com.example.aijournalcompanion.CustomDataTypes.DoublyLinkedList.DoublyLinkedList
import com.example.aijournalcompanion.UI.searchChoices
data class searchContext(
    val type: searchChoices,
    val data: DataState
)
class SearchUtils {
    companion object {
        private fun <T : Comparable<T>> binTreeSearch(
            tree: BinarySearchTree<T>,
            target: T
        ): Node<T>? {
            return tree.search(target)
        }
        private fun <K, V> hashMapSearch(map : HashMap<K,V>, target: K) : V?{
            return map[target]
        }
        private fun <T> doubleLinkedListSearch(list : DoublyLinkedList<T>,target: T) : Int{
            return list.indexOf(target)
        }
        private fun transform( input: String): String{
            return input.trim().lowercase()
        }
        private fun search(input: String,searchContext: searchContext): String {
            return when (searchContext.type) {
                searchChoices.BinaryTree -> binTreeSearch(searchContext.data.tree, input)?.toString() ?: "null: Not found"
                searchChoices.HashBasedMap -> hashMapSearch(searchContext.data.hash, input)?.toString() ?:"null: Not found"
                searchChoices.DoublyLinkedList ->{
                    val r = doubleLinkedListSearch(searchContext.data.list, input)
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