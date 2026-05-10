package com.example.aijournalcompanion

import com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree.BinarySearchTree
import com.example.aijournalcompanion.CustomDataTypes.DoublyLinkedList.DoublyLinkedList

data class DataState(
    val items: List<EmotionResponse>,
    val tree: BinarySearchTree<String>,
    val hash: HashMap<String, Int>,
    val list: DoublyLinkedList<String>
){
    companion object{
        fun from(items: List<EmotionResponse>): DataState {
            return DataState(
                items = items,
                tree = insertBinTree(items),
                hash = HashMap(items.groupingBy { it.emotion }.eachCount()),
                list = DoublyLinkedList<String>().apply { items.forEach { add(it.emotion) } }
            )
        }
        fun insertBinTree(items: List<EmotionResponse>) :  BinarySearchTree<String>{
            val i =  BinarySearchTree<String>()
            items.forEach {
                i.insert(value = it.emotion)
            }
            return i
        }
        val update : (DataState, EmotionResponse)-> DataState = { state, input ->
            val newitems  = state.items + input

            state.copy(
                items = newitems,
                tree = insertBinTree(newitems),
                hash = HashMap(newitems.groupingBy { it.emotion }.eachCount()),
                list = DoublyLinkedList<String>().apply { newitems.forEach { add(it.emotion) } }
            )
        }
    }
}
