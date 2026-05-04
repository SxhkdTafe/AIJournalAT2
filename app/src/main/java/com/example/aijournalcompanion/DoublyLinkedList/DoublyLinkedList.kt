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
    fun addFirst(value: T) {
        val newNode = Node(value)

        if (head == null) {
            head = newNode
            tail = newNode
        } else {
            newNode.next = head
            head!!.prev = newNode
            head = newNode
        }

        size++
    }
    fun remove(value: T): Boolean {
        var current = head

        while (current != null) {
            if (current.value == value) {

                val prevNode = current.prev
                val nextNode = current.next

                if (prevNode != null) {
                    prevNode.next = nextNode
                } else {
                    head = nextNode
                }

                if (nextNode != null) {
                    nextNode.prev = prevNode
                } else {
                    tail = prevNode
                }

                size--
                return true
            }

            current = current.next
        }

        return false
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
    fun printForward() {
        var current = head
        while (current != null) {
            print("${current.value} <-> ")
            current = current.next
        }
        println("null")
    }
    fun printBackward() {
        var current = tail
        while (current != null) {
            print("${current.value} <-> ")
            current = current.prev
        }
        println("null")
    }
}

