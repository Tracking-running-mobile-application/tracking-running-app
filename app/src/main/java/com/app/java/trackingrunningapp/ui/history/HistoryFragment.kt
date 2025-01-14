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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runSessionViewModel.fetchRunSessions(fetchMore = false)
        runSessionViewModel.runSessions.observe(viewLifecycleOwner) { sessions ->
            val mutableList: MutableList<RunSession> = sessions.toMutableList()
            binding.textShowMore.setOnClickListener{
                runSessionViewModel.fetchRunSessions(true)
            }
            runSessionViewModel.hasMoreData.observe(viewLifecycleOwner){hasMore->
                if(hasMore == false){
                    binding.textShowMore.visibility = View.GONE
                }else{
                    binding.textShowMore.visibility = View.VISIBLE
                }
            }
            setupAdapter(mutableList)
        }
        setupToolbarHistory()
        setUpSwipeActions()
    }

    private fun setUpSwipeActions() {
        val touchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.DOWN or ItemTouchHelper.UP,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val deletePosition = viewHolder.layoutPosition
                    val deleteRun = runAdapter.listRuns[deletePosition]
                    runSessionViewModel.deleteRunSession(deleteRun.sessionId)
                    runSessionViewModel.runSessions.observe(viewLifecycleOwner) { sessions ->
                        runAdapter.updateRunHistory(sessions)
                        runAdapter.notifyItemRemoved(deletePosition)
                        val message = "Removed"
                        Snackbar.make(binding.root,message,Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        )
        touchHelper.attachToRecyclerView(binding.rvHistoryDate)
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
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            // TODO: Do something when select start, end date
            val startDate = selection.first // epoch milliseconds
            val endDate = selection.second // epoch milliseconds

            val dateFormatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val formattedStartDate = dateFormatter.format(Date(startDate))
            val formattedEndDate = dateFormatter.format(Date(endDate))
            runSessionViewModel.filterSessionsByDateRange(formattedStartDate,formattedEndDate)
            runSessionViewModel.filteredSession.observe(viewLifecycleOwner) { sessionList ->
                val mutableList: MutableList<RunSession> = sessionList.toMutableList()
                setupAdapter(mutableList)
            }
        }
        dateRangePicker.show(requireActivity().supportFragmentManager, "calendar")
    }
    private fun setupAdapter(runs: MutableList<RunSession>) {
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
                    Log.d("isFav","${itemRun.isFavorite}")
                    Log.d("isHistoryFavourite", "${itemRun.isFavorite}")
                    Snackbar.make(
                        containerLayout,
                        "Successfully Added To Favourite",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (action == RunAdapter.FAVOURITE_REMOVE) {
                    runSessionViewModel.addAndRemoveFavoriteSession(itemRun)
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

    override fun onStop() {
        super.onStop()
        val toolbar = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main)
        // hide filter
        val itemFilter = toolbar.menu.findItem(R.id.item_toolbar_filter)
        itemFilter.isVisible = false
    }
}
