package com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree

import com.example.aijournalcompanion.DataStructs.EmotionResponse

class BinarySearchTree<T: Comparable<T>>() {
    var root: Node<T>? = null
    fun insert(value: T) {
        root = insertRec(root, value)
    }
     fun delete(value: T){
         root = removeRec(root,value)
     }
    fun searchByEmotion(emotion: String): Node<T>? {
        var current = root

        while (current != null) {
            val currentEmotion = (current.value as EmotionResponse).emotion

            val cmp = emotion.compareTo(currentEmotion)

            current = when {
                cmp == 0 -> return current
                cmp < 0 -> current.left
                else -> current.right
            }
        }

        return null
    }
    private fun insertRec(node: Node<T>?, value: T): Node<T> {
        if (node == null) return Node(value)

        when {
            value < node.value -> node.left = insertRec(node.left, value)
            value > node.value -> node.right = insertRec(node.right, value)
        }

        return node
    }
     private fun removeRec(node: Node<T>?, value: T): Node<T>?{
         if (node == null) return null
         when {
             value < node.value -> {
                 node.left = removeRec(node.left, value)
             }

             value > node.value -> {
                 node.right = removeRec(node.right, value)
             }

             else -> {

                 if (node.left == null && node.right == null) {
                     return null
                 }

                 if (node.left == null) {
                     return node.right
                 }

                 if (node.right == null) {
                     return node.left
                 }

                 val successor = findMin(node.right!!)
                 node.value = successor.value
                 node.right = removeRec(node.right, successor.value)
             }
         }

         return node
     }
     private fun findMin(node: Node<T>): Node<T> {
         var current = node

         while (current.left != null) {
             current = current.left!!
         }

         return current
     }
     fun toList(): List<T> {
         val result = mutableListOf<T>()

         fun inorder(node: Node<T>?) {
             if (node == null) return

             inorder(node.left)
             result.add(node.value)
             inorder(node.right)
         }

         inorder(root)
         return result
     }
}