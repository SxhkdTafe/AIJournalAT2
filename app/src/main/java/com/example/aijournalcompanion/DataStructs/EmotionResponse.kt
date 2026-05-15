package com.example.aijournalcompanion.DataStructs

data class EmotionResponse (
    // Unique Id
    val id: String = java.util.UUID.randomUUID().toString(),
    // Emotion from json
    val emotion: String,
    // Advice from Json
    val advice: String,
    // Result for frontend
    val text: String
)  : Comparable<EmotionResponse> {
    // Override for Bin search
    override fun compareTo(other: EmotionResponse): Int {
        return id.compareTo(other.id)
    }
    // Override for linked list search
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EmotionResponse) return false
        return emotion == other.emotion
    }
    // Override for HashMap search
    override fun hashCode(): Int {
        return emotion.hashCode()
    }
}