package com.app.java.trackingrunningapp.ui.profile.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.RunSession
import com.app.java.trackingrunningapp.databinding.FragmentFavouriteRunsBinding
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import com.app.java.trackingrunningapp.utils.StatsUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class FavouriteRuns : Fragment() {
    private lateinit var binding: FragmentFavouriteRunsBinding
    private lateinit var runSessionViewModel: RunSessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteRunsBinding.inflate(inflater, container, false)
        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel =
            ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)
        val toolbarTitle = requireActivity().findViewById<TextView>(R.id.tv_toolbar_title)
        toolbarTitle.text = getString(R.string.text_favourite_run)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        runSessionViewModel.favoriteRunSessions.observe(viewLifecycleOwner) { sessions ->
//            val runList = mutableListOf<RunItem>()
//            for (session in sessions) {
//                if (session.isFavorite) {
//                    val runFavourite = RunItem(
//                        StatsUtils.formatDuration(session.duration ?: 0L),
//                        getString(R.string.text_distance_metric,session.distance ?: 0.0),
//                        DateTimeUtils.formatDateString(session.runDate)
//                    )
//                    runList.add(runFavourite)
//                }
//            }
//            binding.rvFavouriteRun.adapter = FavouriteRunAdapter(runList)
//        }

        runSessionViewModel.loadFavoriteSessions()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                runSessionViewModel.fetchRunSessions()
                runSessionViewModel.favoriteRunSessions.collect{sessions->
                    val runList = mutableListOf<RunItem>()
                    for (session in sessions) {
                        if (session?.isFavorite!!) {
                            val runFavourite = RunItem(
                                StatsUtils.formatDuration(session.duration ?: 0L),
                                getString(R.string.text_distance_metric,session.distance ?: 0.0),
                                DateTimeUtils.formatDateString(session.runDate)
                            )
                            runList.add(runFavourite)
                        }
                    }
                    binding.rvFavouriteRun.adapter = FavouriteRunAdapter(runList)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.VISIBLE
    }
}