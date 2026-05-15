package com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree

import com.example.aijournalcompanion.DataStructs.EmotionResponse

class BinarySearchTree<T: Comparable<T>> {
    // Entire tree
    var root: Node<T>? = null
    // Calls insertion
    fun insert(value: T) {
        root = insertRec(root, value)
    }
    // Calls Deletion
     fun delete(value: T){
         root = removeRec(root,value)
     }
    fun searchByEmotion(emotion: String): Node<T>? {
        var current = root
        while (current != null) {
            // Extracts value out of tree node
            val currentEmotion = (current.value as EmotionResponse).emotion
            // Uses override to compare Emotion Object value
            val cmp = emotion.compareTo(currentEmotion)
            // Reassigns tree to branch
            current = when {
                // Returns found val tree
                cmp == 0 -> return current
                // Goes down left branch if less than
                cmp < 0 -> current.left
                else -> current.right
            }
        }

        return null
    }
    private fun insertRec(node: Node<T>?, value: T): Node<T> {
        // Makes root node value
        if (node == null) return Node(value)

        when {
            // Paths down each branch and finds where value is greater or less than and assigns it there
            value < node.value -> node.left = insertRec(node.left, value)
            value > node.value -> node.right = insertRec(node.right, value)
        }
        // Returns new tree
        return node
    }
     private fun removeRec(node: Node<T>?, value: T): Node<T>?{
         if (node == null) return null
         when {
             // If Value less than target node remove next left node
             value < node.value -> {
                 node.left = removeRec(node.left, value)
             }
             // If Value greater than target node remove next right node
             value > node.value -> {
                 node.right = removeRec(node.right, value)
             }

             else -> {

                 if (node.left == null && node.right == null) {
                     return null
                 }
                 // Makes new searches node right
                 if (node.left == null) {
                     return node.right
                 }
                 // Makes new searched node left
                 if (node.right == null) {
                     return node.left
                 }
                 // Returns left branch of current node
                 val successor = findMin(node.right!!)
                 // Makes node value left branch node value
                 node.value = successor.value
                 // Removes right branch node value with left branch node
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