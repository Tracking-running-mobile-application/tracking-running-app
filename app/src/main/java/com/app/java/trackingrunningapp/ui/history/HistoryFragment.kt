package com.app.java.trackingrunningapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R

class HistoryFragment : Fragment() {

    private lateinit var runDateAdapter: RunDateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val runRecyclerView: RecyclerView = view.findViewById(R.id.runRecyclerView)
        runRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val runDates = generateSampleData() // Replace with your data source
        runDateAdapter = RunDateAdapter(runDates)
        runRecyclerView.adapter = runDateAdapter

        return view
    }

    private fun generateSampleData(): List<RunDate> {
        val octoberRuns = listOf(
            Run("Normal Run", "12KM", "Today"),
            Run("Long Run", "20KM", "Yesterday")
        )
        val septemberRuns = listOf(
            Run("Short Run", "5KM", "3 Days Ago"),
            Run("Normal Run", "8KM", "Last Week")
        )
        val augustRuns = listOf(
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month")
        )

        return listOf(
            RunDate("OCTOBER 2024", octoberRuns),
            RunDate("SEPTEMBER 2024", septemberRuns),
            RunDate("AUGUST 2024", augustRuns)
        )
    }

}
