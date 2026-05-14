package com.example.aijournalcompanion.CustomDataTypes.DoublyLinkedList

import com.example.aijournalcompanion.DataStructs.EmotionResponse

class DoublyLinkedList<T> {
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
    fun delete(value: T) {
        var current = head
        while (current != null) {
            if (current.value == value) {

                if (current == head) {
                    head = current.next
                    head?.prev = null
                }
                else if (current == tail) {
                    tail = current.prev
                    tail?.next = null
                }

                else {
                    current.prev?.next = current.next
                    current.next?.prev = current.prev
                }
                size--
                if (size == 0) {
                    head = null
                    tail = null
                }
                return
            }
            current = current.next
        }
    }
    fun indexOfEmotion(emotion: String): Int {
        var current = head
        var index = 0

        while (current != null) {
            if ((current.value as EmotionResponse).emotion == emotion) return index
            current = current.next
            index++
        }

        return -1
    }
    fun toList(): List<T> {
        val result = mutableListOf<T>()
        var current = head

        while (current != null) {
            result.add(current.value)
            current = current.next
        }

        return result
    }
}

