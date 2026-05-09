package com.example.aijournalcompanion.CustomDataTypes.DoublyLinkedList

data class Node<T>(
    var value: T,
    var next: Node<T>? = null,
    var prev: Node<T>? = null
)
