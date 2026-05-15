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
            // Flattens collection
            val list = inputList.toMutableList()
            val n = list.size
            var swapped: Boolean
            // Outside loop
            for (i in 0 until n - 1) {
                swapped = false
                // Inside loop
                for (j in 0 until n - i - 1) {
                    // Swaps if emotion greater than next
                    if (list[j].emotion > list[j + 1].emotion) {

                        val temp = list[j]
                        list[j] = list[j + 1]
                        list[j + 1] = temp
                        swapped = true
                    }
                }
                // Goes to next index if none swapped
                if (!swapped) break
            }
            // Returns sorted list
            return list
        }
        private fun insertionSortStrings(inputList: Collection<EmotionResponse>) : List<EmotionResponse>{
            // Flattens collection
            val list =  inputList.toMutableList()
            val n = list.size
            // Continues until max list size is reached
            for (i in 1 until n ){
                val key = list[i]
                var j = i - 1
                // Continues until j is not less than zero and current item does not equal target item
                while (j >= 0 && list[j].emotion.compareTo(key.emotion) > 0){
                    // Next item is equal to current item
                    list[j+1] = list[j]
                    // Decrements
                    j--
                }
                // Replaces next item with match
                list[j+1] = key
            }
            return list
        }
        private fun selectionSortStrings(inputList: Collection<EmotionResponse>): List<EmotionResponse>{
            // Flattens collection
            val list = inputList.toMutableList()
            for (i in 0 until list.size - 1) {
                var minIndex = i

                for (j in i + 1 until list.size) {
                    // Compares min value in list to current and moves min val up if less
                    if (list[j].emotion < list[minIndex].emotion) {
                        minIndex = j
                    }
                }
                // Reassigns current to value that is greater
                val temp = list[i]
                list[i] = list[minIndex]
                list[minIndex] = temp
            }

            return list
        }
        fun sort(type: sortChoices, datatype: searchChoices, inputData: DataState): List<EmotionResponse> {
            // Data Type to be flattered for sorting
            val data : List<EmotionResponse> = when (datatype){
                searchChoices.BinaryTree -> inputData.tree.toList()
                searchChoices.HashBasedMap -> inputData.hash.keys.toList()
                searchChoices.DoublyLinkedList -> inputData.list.toList()
                searchChoices.SelectSearchChoice -> inputData.list.toList()
            }
            // Sorting Algorithm used by frontend enum choice
            return when (type) {
                sortChoices.BubbleSort -> bubbleSortStrings( data)
                sortChoices.InsertionSort -> insertionSortStrings(data)
                sortChoices.SelectionSort -> selectionSortStrings(data)
                sortChoices.SelectSortChoice -> data
            }
        }
    }
}