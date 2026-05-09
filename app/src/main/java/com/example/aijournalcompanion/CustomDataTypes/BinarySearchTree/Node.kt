package com.example.aijournalcompanion.CustomDataTypes.BinarySearchTree

data class Node<T>(    var value: T,
                       var left: Node<T>? = null,
                       var right: Node<T>? = null)