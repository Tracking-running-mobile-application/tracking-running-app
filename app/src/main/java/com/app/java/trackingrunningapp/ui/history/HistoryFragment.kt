package com.app.java.trackingrunningapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentHistoryBinding
import com.app.java.trackingrunningapp.ui.history.adapter.RunDateAdapter
import com.google.android.material.datepicker.MaterialDatePicker

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
        // hide setting, show filter
        val toolbar = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main)
        val itemSetting = toolbar.menu.findItem(R.id.item_toolbar_setting)
        itemSetting.isVisible = false
        val itemFilter =  toolbar.menu.findItem(R.id.item_toolbar_filter)
        itemFilter.isVisible = true
        itemFilter.setOnMenuItemClickListener {
            showCalendar()
            true
        }
    }
    private fun showCalendar() {
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Dates")
                .setTheme(R.style.ThemeMaterialCalendar)
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                ).build()
        dateRangePicker.addOnPositiveButtonClickListener {
            // TODO: Do something when select start, end date 
        }
        dateRangePicker.show(requireActivity().supportFragmentManager,"calendar")
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
        // hide setting
        val itemSetting = toolbar.menu.findItem(R.id.item_toolbar_setting)
        itemSetting.isVisible = true
        // hide filter
        val itemFilter =  toolbar.menu.findItem(R.id.item_toolbar_filter)
        itemFilter.isVisible = false
        // pop to profile
        this.findNavController().popBackStack(R.id.profileFragment, false)
    }
}
