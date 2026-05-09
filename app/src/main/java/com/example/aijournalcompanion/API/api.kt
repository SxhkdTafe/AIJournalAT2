package com.example.aijournalcompanion.API

import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URI
import kotlin.collections.get

class Api {
    suspend fun sendToBackend(text: String,apiUrl: String): JsonObject {
        return withContext(Dispatchers.IO) {
            try {
                val gson = Gson()
                val uri = URI(apiUrl)
                val conn = uri.toURL().openConnection() as HttpURLConnection

                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true

                val json = gson.toJson(mapOf("text" to text))
                conn.outputStream.use {
                    it.write(json.toByteArray())
                }

                val response = conn.inputStream.bufferedReader().readText()
                gson.fromJson(response, JsonObject::class.java)
            } catch (e: Exception) {
                JsonObject().apply {
                    addProperty("error", e.message)
                }
            }
        }
    }
}