package com.app.java.trackingrunningapp.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.dataclass.history.Run
import com.app.java.trackingrunningapp.data.model.dataclass.history.RunDate
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.databinding.FragmentHistoryBinding
import com.app.java.trackingrunningapp.ui.history.adapter.RunAdapter
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var runAdapter: RunAdapter
    private lateinit var containerLayout: View
    private lateinit var runSessionViewModel: RunSessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel =
            ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)

        //IMPORTANT: fetchMore should be false initially,
        //it should only be true when the user click show more !!

//        runSessionViewModel.fetchRunSessions(fetchMore = false)
//        setUpRefreshing()
        runSessionViewModel.fetchRunSessions(fetchMore = false)
        runSessionViewModel.runSessions.observe(viewLifecycleOwner) { sessions ->
            binding.textShowMore.setOnClickListener{
                runSessionViewModel.fetchRunSessions(true)
            }
            setupAdapter(sessions)
        }

        // hasMoreData == true then show more option appears, else disappear
        runSessionViewModel.hasMoreData.observe(viewLifecycleOwner) { hasMoreData ->
            if (hasMoreData) {
                Log.d(
                    "History Fragment Log hasMoreData",
                    "if $hasMoreData then there is still more run sessions in the database"
                )
            } else {
                Log.e("Error", "No run sessions found in the database (hasMoreData)")
            }
        }

        return binding.root
    }

    private fun setupAdapter(runs: List<RunSession>) {
        containerLayout = binding.containerLayoutHistory
        runAdapter = RunAdapter(runs, requireContext(),object : OnItemHistoryRunClickListener {
            override fun onItemClick(itemRun: RunSession) {
                val bundle = Bundle().apply {
                    putInt(DetailRunFragment.EXTRA_HISTORY_RUN_ID,itemRun.sessionId)
                }
                findNavController().navigate(R.id.action_historyFragment_to_detailRunFragment,bundle)
                requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility =
                    View.GONE
            }
            override fun onAddFavouriteClick(action: Int,itemRun: RunSession) {
                if (action == RunAdapter.FAVOURITE_ADD) {
                    runSessionViewModel.addAndRemoveFavoriteSession(itemRun)
                    Log.d("isHistoryFavourite", "${itemRun.isFavorite}")
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
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility =
            View.VISIBLE
//        runDateAdapter.updateRunDate(runDates)
        binding.rvHistoryDate.adapter = runAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onStop() {
        super.onStop()
        val toolbar = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main)
        // hide filter
        val itemFilter = toolbar.menu.findItem(R.id.item_toolbar_filter)
        itemFilter.isVisible = false
    }
}
