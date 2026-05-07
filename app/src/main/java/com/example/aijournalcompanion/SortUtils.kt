package com.example.aijournalcompanion

class SortUtils {
    companion object{
        fun bubbleSortStrings(inputList: List<String>) : List<String> {
            var list = inputList.toMutableList()
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

    }
}