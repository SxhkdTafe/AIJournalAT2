package com.example.aijournalcompanion.DataStructs

data class EmotionResponse (
    val id: String = java.util.UUID.randomUUID().toString(),
    val emotion: String,
    val advice: String,
    val text: String
)  : Comparable<EmotionResponse> {

    override fun compareTo(other: EmotionResponse): Int {
        return id.compareTo(other.id)
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EmotionResponse) return false
        return emotion == other.emotion
    }
    override fun hashCode(): Int {
        return emotion.hashCode()
    }
}