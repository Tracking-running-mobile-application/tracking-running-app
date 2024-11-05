package com.app.java.trackingrunningapp.ui.feature_dailyTask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.app.java.trackingrunningapp.R

class DailyTask : Fragment() {
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_task, container, false)
        val spinner: Spinner = view.findViewById(R.id.spinner)
        val items = listOf("Every Monday", "Every Tuesday", "Every Wednesday", "Every Thursday","Every Friday","Every Saturday","Every Sunday")
        val adapter = ArrayAdapter(requireContext(),R.layout.spinner_item, items)

        adapter.setDropDownViewResource(R.layout.spinner_item)

        spinner.adapter = adapter

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Get the selected item
                val selectedItem = parent.getItemAtPosition(position) as String
                // Do something with the selected item
                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
        return view
    }
}