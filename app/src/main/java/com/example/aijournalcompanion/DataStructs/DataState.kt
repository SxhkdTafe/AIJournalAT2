package com.example.aijournalcompanion.DataStructs

import com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree.BinarySearchTree
import com.example.aijournalcompanion.CustomDataTypes.DoublyLinkedList.DoublyLinkedList

data class DataState(
    val tree: BinarySearchTree<EmotionResponse>,
    val hash: HashMap<EmotionResponse, Int>,
    val list: DoublyLinkedList<EmotionResponse>
){

    fun update(item: EmotionResponse) {
        tree.insert(item)
        hash[item] = (hash[item] ?: 0) + 1
        list.add(item)
    }
    fun delete(item: EmotionResponse){
        tree.delete(item)
        hash.remove(item)
        list.delete(item)
    }
    fun toList(): List<EmotionResponse> {
        return list.toList()
    }

    companion object{

        fun from(): DataState {
            return DataState(
                tree = BinarySearchTree(),
                hash = HashMap(),
                list = DoublyLinkedList()
            )
        }
        fun rebuild(items: Collection<EmotionResponse>): DataState {
            val state = from()
            items.forEach {
                state.update(it)
            }
            return state
        }
    }
}