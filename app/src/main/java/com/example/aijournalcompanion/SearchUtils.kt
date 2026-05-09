package com.example.aijournalcompanion

import com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree.BinarySearchTree
import com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree.Node
import com.example.aijournalcompanion.CustomDataTypes.DoublyLinkedList.DoublyLinkedList
import com.example.aijournalcompanion.PIPELINES.Context
import com.example.aijournalcompanion.UI.searchChoices

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
        fun search(type: searchChoices, input: String, data: DataState): String {
            return when (type) {
                searchChoices.BinaryTree -> binTreeSearch(data.tree, input).toString()
                searchChoices.HashBasedMap -> hashMapSearch(data.hash, input).toString()
                searchChoices.DoublyLinkedList -> doubleLinkedListSearch(data.list, input).toString()
                searchChoices.DEFAULT -> input
            }
        }
    }
}