package com.example.aijournalcompanion

data class EmotionResponse (
    val emotion: String,
    val advice: String,
    val text: String
) : Comparable<EmotionResponse> {

    override fun compareTo(other: EmotionResponse): Int {
        return emotion.compareTo(other.emotion)
    }
}