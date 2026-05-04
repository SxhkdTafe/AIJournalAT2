package com.example.aijournalcompanion

import com.example.aijournalcompanion.BinarySearchTree.Node
import com.example.aijournalcompanion.DoublyLinkedList.DoublyLinkedList
import java.util.LinkedList


class SearchUtils {
    fun <T : Comparable<T>> binTreeSearch(root: Node<T>?, target: T): Node<T>? {
        var current = root

        while (current != null) {
            current = when {
                target == current.value -> return current
                target < current.value  -> current.left
                else                    -> current.right
            }
        }

        return null
    }
    fun <K, V> hashMapSearch(map : HashMap<K,V>, target: K) : V?{
        return map[target]
    }
    fun <T> doubleLinkedListSearch(list : DoublyLinkedList<T>,target: T) : Int{
        return list.indexOf(target)
    }
}