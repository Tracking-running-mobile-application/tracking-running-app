package com.app.java.trackingrunningapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentHistoryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding

    private lateinit var runDateAdapter: RunDateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val runDates = generateSampleData()
        val limitedRunDates = limitToMaxItems(runDates, 20)

        runDateAdapter = RunDateAdapter(limitedRunDates)
        binding.rvHistoryDate.adapter = runDateAdapter
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
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
            Run("Short Run", "5KM", "Last Month"),
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            Run("Ultra Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            Run("Long Run", "20KM", "Last Month"),
            Run("Short Run", "5KM", "Last Month"),
            )

        return listOf(
            RunDate("OCTOBER 2024", octoberRuns),
            RunDate("SEPTEMBER 2024", septemberRuns),
            RunDate("AUGUST 2024", augustRuns)
        )
    }

    private fun refreshData() {
        val newRunDates = generateSampleData() // Replace with API or database fetch logic
        val limitedRunDates = limitToMaxItems(newRunDates, 20)

        runDateAdapter = RunDateAdapter(limitedRunDates)
        binding.rvHistoryDate.adapter = runDateAdapter

        // Stop the refresh indicator
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun limitToMaxItems(runDates: List<RunDate>, maxItems: Int): List<RunDate> {
        val flattenedRuns = runDates.flatMap { runDate ->
            runDate.runs.map { run -> runDate.date to run }
        }
        val limitedRuns = flattenedRuns.take(maxItems)
        val groupedRuns = limitedRuns.groupBy { it.first }.map { (date, runs) ->
            RunDate(date, runs.map { it.second })
        }

        return groupedRuns
    }
    override fun onStop() {
        super.onStop()
        val toolbar = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main)

        val itemFilter = toolbar.menu.findItem(R.id.item_toolbar_filter)
        itemFilter.isVisible = false

        val itemSetting = toolbar.menu.findItem(R.id.item_toolbar_setting)
        itemSetting.isVisible = true

        // pop to profile
        this.findNavController().popBackStack(R.id.profileFragment,false)
    }
}
