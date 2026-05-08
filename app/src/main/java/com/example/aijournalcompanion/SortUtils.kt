package com.example.aijournalcompanion

import kotlin.collections.toMutableList

class SortUtils {
    companion object{
        fun bubbleSortStrings(inputList: Collection<String>) : List<String> {
            val list = inputList.toMutableList()
            val n = list.size
            var swapped: Boolean

            for (i in 0 until n - 1) {
                swapped = false
                for (j in 0 until n - i - 1) {
                    if (list[j] > list[j + 1]) {

                        val temp = list[j]
                        list[j] = list[j + 1]
                        list[j + 1] = temp
                        swapped = true
                    }
                }
                if (!swapped) break
            }
            return list
        }
        fun insertionSortStrings(inputList: Collection<String>) : List<String>{
            val list =  inputList.toMutableList()
            val sortedList = mutableListOf<String>()
            var currentElement = list [1]
            var j = 1
            while(j <= list.size){
                if (currentElement.compareTo(list[j]) > 0){
                    sortedList.add(list[j])
                    currentElement = list[j]
                    j++
                }
                else{
                    sortedList.add(currentElement)
                    currentElement = list[j]
                    j++
                }
            }
            return sortedList
        }
        fun selectionSortStrings(inputList: Collection<String>): List<String>{
            val list = inputList.toMutableList()
            for (i in 0 until list.size - 1) {
                var minIndex = i

                for (j in i + 1 until list.size) {
                    if (list[j] < list[minIndex]) {
                        minIndex = j
                    }
                }

                val temp = list[i]
                list[i] = list[minIndex]
                list[minIndex] = temp
            }

            return list
        }

    }
}