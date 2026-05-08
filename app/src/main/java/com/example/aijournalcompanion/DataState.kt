package com.example.aijournalcompanion

import com.example.aijournalcompanion.BinarySearchTree.BinarySearchTree
import com.example.aijournalcompanion.DoublyLinkedList.DoublyLinkedList

data class DataState(
    val items: List<String>,
    val tree: BinarySearchTree<String>,
    val hash: HashMap<String, Int>,
    val list: DoublyLinkedList<String>
){
    companion object{
        fun insertBinTree(items: List<String>) :  BinarySearchTree<String>{
            val i =  BinarySearchTree<String>()
            items.forEach {
                i.insert(value = it)
            }
            return i
        }
        val update : (DataState,String)-> DataState = { state,input ->
            val newitems  = state.items + input

            state.copy(
                items = newitems,
                tree = insertBinTree(newitems),
                hash = HashMap(newitems.associateWith { it.length }),
                list = DoublyLinkedList<String>().apply { newitems.forEach { add(it) } }
            )
        }
    }

}
