package com.example.aijournalcompanion.DoublyLinkedList

class DoublyLinkedList<T>{
    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    private var size = 0
    fun add(value: T) {
        val newNode = Node(value)

        if (head == null) {
            head = newNode
            tail = newNode
        } else {
            tail!!.next = newNode
            newNode.prev = tail
            tail = newNode
        }

        size++
    }
    fun indexOf(value: T): Int {
        var current = head
        var index = 0

        while (current != null) {
            if (current.value == value) return index
            current = current.next
            index++
        }

        return -1
    }
}

