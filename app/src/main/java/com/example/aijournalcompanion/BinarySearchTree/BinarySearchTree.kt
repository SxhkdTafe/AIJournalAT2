package com.example.aijournalcompanion.BinarySearchTree

class BinarySearchTree<T: Comparable<T>>() {
    var root: Node<T>? = null
    fun <T : Comparable<T>> insert(root: Node<T>?, value: T): Node<T> {
        if (root == null) return Node(value)

        when {
            value < root.value -> root.left = insert(root.left, value)
            value > root.value -> root.right = insert(root.right, value)
        }

        return root
    }
}