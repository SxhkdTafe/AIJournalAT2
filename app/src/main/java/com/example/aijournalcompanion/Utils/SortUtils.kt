package com.example.aijournalcompanion.Utils

import com.example.aijournalcompanion.DataStructs.DataState
import com.example.aijournalcompanion.DataStructs.EmotionResponse
import com.example.aijournalcompanion.UI
import kotlin.collections.toMutableList
import com.example.aijournalcompanion.UI.sortChoices
import com.example.aijournalcompanion.UI.searchChoices

class SortUtils {
    companion object{
        private fun bubbleSortStrings(inputList: Collection<EmotionResponse>) : List<EmotionResponse> {
            val list = inputList.toMutableList()
            val n = list.size
            var swapped: Boolean

            for (i in 0 until n - 1) {
                swapped = false
                for (j in 0 until n - i - 1) {
                    if (list[j].emotion > list[j + 1].emotion) {

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
        private fun insertionSortStrings(inputList: Collection<EmotionResponse>) : List<EmotionResponse>{
            val list =  inputList.toMutableList()
            val n = list.count()
            for (i in 1 until n ){
                val key = list[i]
                var j = i -1
                while (j >= 0 && list[j].emotion.compareTo(key.emotion) > 0){
                    list[j+1] = list[j]
                    j--
                }
                list[j+1] = key
            }
            return list
        }
        private fun selectionSortStrings(inputList: Collection<EmotionResponse>): List<EmotionResponse>{
            val list = inputList.toMutableList()
            for (i in 0 until list.size - 1) {
                var minIndex = i

                for (j in i + 1 until list.size) {
                    if (list[j].emotion < list[minIndex].emotion) {
                        minIndex = j
                    }
                }

                val temp = list[i]
                list[i] = list[minIndex]
                list[minIndex] = temp
            }

            return list
        }
        fun sort(type: sortChoices, datatype: searchChoices, inputData: DataState): List<EmotionResponse> {
            val data : List<EmotionResponse> = when (datatype){
                searchChoices.BinaryTree -> inputData.tree.toList()
                searchChoices.HashBasedMap -> inputData.hash.keys.toList()
                searchChoices.DoublyLinkedList -> inputData.list.toList()
                searchChoices.SelectSearchChoice -> inputData.list.toList()
            }
            return when (type) {
                sortChoices.BubbleSort -> bubbleSortStrings( data)
                sortChoices.InsertionSort -> insertionSortStrings(data)
                sortChoices.SelectionSort -> selectionSortStrings(data)
                sortChoices.SelectSortChoice -> data
            }
        }
    }
}