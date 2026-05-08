package com.example.aijournalcompanion.API

import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URI
import kotlin.collections.get

class Api {
    suspend fun sendToBackend(text: String,apiUrl: String): String {
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
                val obj = gson.fromJson(response, Map::class.java)

                obj["Advice"]?.toString() ?: "(No response)"
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
    }
}