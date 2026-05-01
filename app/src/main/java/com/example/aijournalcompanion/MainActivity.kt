package com.example.aijournalcompanion

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.net.HttpURLConnection
import java.net.URI
import com.google.gson.Gson


class MainActivity : AppCompatActivity(),  OnItemSelectedListener {
    private var searchChoices = arrayOf(
        "Binary Tree", "Hash-based(Map)", "Doubly Linked List"
    )
    private var sortChoices = arrayOf(
        "Bubble Sort", "Insertion Sort", "Selection Sort"
    )
    private val listItems = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiUrl = "http://127.0.0.1:8000/emotion_parse"

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val showButton = findViewById<Button>(R.id.Show_button)
        val searchSpin = findViewById<Spinner>(R.id.Search_spinner)
        val sortSpin = findViewById<Spinner>(R.id.Sort_spinner)
        val userInput = findViewById<EditText>(R.id.editText)
        val analyseBtn = findViewById<Button>(R.id.Analyse_button)
        val journalListView = findViewById<ListView>(R.id.journalListView)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        val adSearch: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_item, searchChoices
        )
        val adSort: ArrayAdapter<*> = ArrayAdapter<Any?>(this,
            android.R.layout.simple_spinner_item, sortChoices
        )
        adSearch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        searchSpin.adapter = adSearch
        sortSpin.adapter = adSort


        analyseBtn.setOnClickListener {
            val result = displayInput(userInput, apiUrl)
            listItems.add(result)
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,listItems)
            journalListView.adapter= adapter
            resultTextView.text = result
        }
        showButton.setOnClickListener{
            showPopup()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long)
    {
        Toast.makeText(applicationContext, searchChoices[position], Toast.LENGTH_SHORT).show()
        Toast.makeText(applicationContext, sortChoices[position], Toast.LENGTH_SHORT).show()
    }

    override  fun onNothingSelected(parent: AdapterView<*>?) {}
    private fun showPopup() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_layout)
        val chart = dialog.findViewById<PieChart>(R.id.pieChart)
        val btnClose = dialog.findViewById<Button>(R.id.btnClose)

        val entry = ArrayList<PieEntry>()
        entry.add(PieEntry(50f, "Lmao"))
        entry.add(PieEntry(50f, "Lmao222"))
        val dataset = PieDataSet(entry, "lol")
        dataset.colors = listOf(Color.BLUE, Color.RED)

        val data = PieData(dataset)
        data.setValueTextColor(Color.GREEN)

        chart.data = data

        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun displayInput(box: EditText, url: String) : String{
        if(box.text == null || box.text.isEmpty()){
            return "Textbox is empty"
        }
        val input = box.text.trim().toString()
        try {
            val gson = Gson()
            val uri = URI(url)
            val conn = uri.toURL().openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doInput = true
            val json = gson.toJson(mapOf("text" to input))
            conn.getOutputStream().use {it.write((json.toByteArray()))}

            val response = conn.getInputStream().bufferedReader().readText()
            val obj = gson.fromJson(response, Map::class.java)
            return obj["Advice"]?.toString() ?: "(No response)"
        }
        catch (e : Exception){
            return "Error: couldn't connect to server | $e "
        }
    }
}