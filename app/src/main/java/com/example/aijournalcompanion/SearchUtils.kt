package com.example.aijournalcompanion

import com.example.aijournalcompanion.BinarySearchTree.BinarySearchTree
import com.example.aijournalcompanion.BinarySearchTree.Node
import com.example.aijournalcompanion.DoublyLinkedList.DoublyLinkedList
import java.util.LinkedList


class SearchUtils {
    companion object {
        fun <T : Comparable<T>> binTreeSearch(
            tree: BinarySearchTree<T>,
            target: T
        ): Node<T>? {
            return tree.search(target)
        }
        fun <K, V> hashMapSearch(map : HashMap<K,V>, target: K) : V?{
            return map[target]
        }
        fun <T> doubleLinkedListSearch(list : DoublyLinkedList<T>,target: T) : Int{
            return list.indexOf(target)
        }
    }


}