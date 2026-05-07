package com.example.aijournalcompanion.BinarySearchTree
 class BinarySearchTree<T: Comparable<T>>() {
    var root: Node<T>? = null
    fun insert(value: T) {
        root = insertRec(root, value)
    }
     fun search(target: T): Node<T>? {
         var current = root

         while (current != null) {
             current = when {
                 target == current.value -> return current
                 target < current.value -> current.left
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
}