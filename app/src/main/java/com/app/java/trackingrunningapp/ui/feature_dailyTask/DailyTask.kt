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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_daily_task, container, false)

        // Find the Spinner in the layout
        val spinner: Spinner = view.findViewById(R.id.spinner)

        // Create a list of items to display in the Spinner
        val items = listOf("Every Monday", "Every Tuesday", "Every Wednesday", "Every Thursday","Every Friday","Every Saturday","Every Sunday")

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner.adapter = adapter

        // Set a listener to handle item selection
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Get the selected item
                val selectedItem = parent.getItemAtPosition(position) as String
                // Do something with the selected item
                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected (optional)
            }
        })


        return view

    }
}