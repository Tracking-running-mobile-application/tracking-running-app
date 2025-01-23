package com.app.java.trackingrunningapp.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentProfileBinding
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.StatsViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.StatsViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import com.app.java.trackingrunningapp.utils.StatsUtils
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var runSessionViewModel: RunSessionViewModel
    private lateinit var statsViewModel: StatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userFactory = UserViewModelFactory(UserRepository())
        userViewModel = ViewModelProvider(requireActivity(), userFactory)[UserViewModel::class.java]
        userViewModel.fetchUserInfo()
        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel =
            ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)
        val statsFactory = StatsViewModelFactory(InitDatabase.statsRepository)
        statsViewModel =
            ViewModelProvider(requireActivity(), statsFactory)[StatsViewModel::class.java]
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.userLiveData.observe(viewLifecycleOwner) {
            binding.textProfileName.text = it?.name
            binding.textProfileAge.text = it?.age.toString()
            binding.textProfileWeight.text = getString(R.string.profile_weight, it?.weight)
            binding.textProfileHeight.text =
                getString(R.string.profile_height, it?.height?.times(0.01))
            binding.textUserWeightMetric.text = it?.unit.toString()

            val currentMonth = DateTimeUtils.getCurrentDate().month
            statsViewModel.refreshStats()
            statsViewModel.currentYearStats.observe(viewLifecycleOwner) { sessions ->
                for (session in sessions) {
                    if (DateTimeUtils.getMonthNameFromYearMonth(session.yearlyStatsKey) == currentMonth.toString()) {
                        binding.textProfileSpeed.text =
                            StatsUtils.convertToPace(
                                session.totalAvgSpeed!!,
                                it?.metricPreference!!
                            )

                    }
                }
            }
        }

        setupBarChart()
        navigateToFavourite()
    }

    @SuppressLint("SetTextI18n")
    private fun navigateToFavourite() {
        runSessionViewModel.loadFavoriteSessions()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                runSessionViewModel.fetchRunSessions()
                runSessionViewModel.favoriteRunSessions.collect { favouriteRuns ->
                    binding.textFavouriteRun.text = favouriteRuns.size.toString()
                    Log.d("Favourite", "${favouriteRuns.size}")
                    binding.cvFavoriteRun.setOnClickListener {
                        if (favouriteRuns.isEmpty()) {
                            findNavController().navigate(R.id.action_profileFragment_to_noFavouriteFragment)
                        } else {
                            findNavController().navigate(R.id.action_profileFragment_to_favouriteRuns)
                        }
                    }
                }
            }
        }
//        runSessionViewModel.fetchRunSessions()
//        runSessionViewModel.runSessions.observe(viewLifecycleOwner){sessions->
//            var favouritesRun = 0
//            for (session in sessions){
//                if(session.isFavorite){
//                    favouritesRun++
//                }
//            }
//            Log.d("Favourite","${favouritesRun}")
//            binding.textFavouriteRun.text = favouritesRun.toString()
//            binding.cvFavoriteRun.setOnClickListener {
//                if(favouritesRun == 0){
//                    findNavController().navigate(R.id.action_profileFragment_to_noFavouriteFragment)
//                }else{
//                    findNavController().navigate(R.id.action_profileFragment_to_favouriteRuns)
//                }
//            }
//        }
    }

    private fun setupBarChart() {
        val barSet = listOf(
            "Mon" to 4F,
            "Tue" to 2F,
            "Wed" to 4.5F,
            "Thu" to 3F,
            "Fri" to 6F,
            "Sat" to 1.5F,
            "Sun" to 4F
        )
        val barChart = binding.barchart
        barChart.animate(barSet)
        barChart.apply {
            animation.duration = 1000L
            labelsFormatter = { it.toInt().toString() }
        }
    }
}