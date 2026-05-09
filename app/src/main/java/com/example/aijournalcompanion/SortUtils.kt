package com.example.aijournalcompanion

import kotlin.collections.toMutableList
import com.example.aijournalcompanion.UI.sortChoices

class SortUtils {
    companion object{
        private fun bubbleSortStrings(inputList: Collection<String>) : List<String> {
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
        private fun insertionSortStrings(inputList: Collection<String>) : List<String>{
            val list =  inputList.toMutableList()
            val n = list.count()
            for (i in 1 until n ){
                val key = list[i]
                var j = i -1
                while (j >= 0 && list[j].compareTo(key) > 0){
                    list[j+1] = list[j]
                    j--
                }
                list[j+1] = key
            }
            return list
        }
        private fun selectionSortStrings(inputList: Collection<String>): List<String>{
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
        fun sort(type: sortChoices, data: List<String>): List<String> {
            return when (type) {
                sortChoices.BubbleSort -> bubbleSortStrings(data)
                sortChoices.InsertionSort -> insertionSortStrings(data)
                sortChoices.SelectionSort -> selectionSortStrings(data)
                sortChoices.DEFAULT -> data.sorted()
            }
        }
    }
}