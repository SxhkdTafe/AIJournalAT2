package com.example.aijournalcompanion

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class MainActivity : AppCompatActivity(),  OnItemSelectedListener {
    private var searchChoices = arrayOf(
        "Binary Tree", "Hash-based(Map)", "Doubly Linked List"
    )
    private var sortChoices = arrayOf(
        "Bubble Sort", "Insertion Sort", "Selection Sort"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val showButton = findViewById<Button>(R.id.Show_button)
        val searchSpin = findViewById<Spinner>(R.id.Search_spinner)
        val sortSpin = findViewById<Spinner>(R.id.Sort_spinner)
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
}