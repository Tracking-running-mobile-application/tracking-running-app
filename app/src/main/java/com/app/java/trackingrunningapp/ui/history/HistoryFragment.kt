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
import com.app.java.trackingrunningapp.data.model.dataclass.history.Run
import com.app.java.trackingrunningapp.data.model.dataclass.history.RunDate
import com.app.java.trackingrunningapp.databinding.FragmentHistoryBinding
import com.app.java.trackingrunningapp.ui.history.adapter.RunAdapter
import com.app.java.trackingrunningapp.ui.history.adapter.RunDateAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var runDateAdapter: RunDateAdapter
    private lateinit var runAdapter: RunAdapter
    private lateinit var containerLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerHistory()
        setupToolbarHistory()
    }

    private fun setupToolbarHistory() {
        // hide setting, show filter
        val toolbar = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main)
        val itemSetting = toolbar.menu.findItem(R.id.item_toolbar_setting)
        itemSetting.isVisible = false
        val itemFilter = toolbar.menu.findItem(R.id.item_toolbar_filter)
        itemFilter.isVisible = true
        itemFilter.setOnMenuItemClickListener {
            showCalendar()
            true
        }
    }

    private fun setupRecyclerHistory() {
        setupRunDate()
        setUpRefreshing()
    }

    private fun setUpRefreshing() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            val newRunDates = generateSampleData() // Replace with API or database fetch logic
            // update
            runDateAdapter.updateRunDate(newRunDates)
            // Stop the refresh indicator
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupRunDate() {
        val runDates = generateSampleData()
        containerLayout = binding.containerLayoutHistory
        runDateAdapter = RunDateAdapter(object : OnItemHistoryRunClickListener {
            override fun onItemClick(itemRun: Run) {
                findNavController().navigate(R.id.action_historyFragment_to_detailRunFragment)
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE
            }
            override fun onAddFavouriteClick(action: Int) {
                if (action == RunAdapter.FAVOURITE_ADD) {
                    Snackbar.make(
                        containerLayout,
                        "Successfully Added To Favourite",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (action == RunAdapter.FAVOURITE_REMOVE) {
                    Snackbar.make(
                        containerLayout,
                        "Successfully Removed From Favourite",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.VISIBLE
        runDateAdapter.updateRunDate(runDates)
        binding.rvHistoryDate.adapter = runDateAdapter
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
        dateRangePicker.show(requireActivity().supportFragmentManager, "calendar")
    }

    private fun generateSampleData(): List<RunDate> {
        val octoberRuns = listOf(
            Run("00:30:10", "12KM"),
            Run("00:30:10", "20KM")
        )
        val septemberRuns = listOf(
            Run("00:30:10", "12KM"),
            Run("00:30:10", "12KM")
        )
        val augustRuns = listOf(
            Run("00:30:10", "12KM"),
            Run("00:30:10", "12KM")
        )

        return listOf(
            RunDate("OCTOBER 2024", octoberRuns),
            RunDate("SEPTEMBER 2024", septemberRuns),
            RunDate("AUGUST 2024", augustRuns)
        )
    }

    override fun onStop() {
        super.onStop()
        val toolbar = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main)
        // hide filter
        val itemFilter = toolbar.menu.findItem(R.id.item_toolbar_filter)
        itemFilter.isVisible = false
    }
}
