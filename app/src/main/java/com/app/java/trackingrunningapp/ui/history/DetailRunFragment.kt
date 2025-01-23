package com.app.java.trackingrunningapp.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.databinding.FragmentDetailRunBinding
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
import com.app.java.trackingrunningapp.utils.DateTimeUtils
import com.app.java.trackingrunningapp.utils.StatsUtils


class DetailRunFragment : Fragment() {
    private lateinit var binding: FragmentDetailRunBinding
    private lateinit var runSessionViewModel: RunSessionViewModel
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailRunBinding.inflate(inflater, container, false)
        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel =
            ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)

        val userFactory = UserViewModelFactory(InitDatabase.userRepository)
        userViewModel = ViewModelProvider(this,userFactory)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val runSessionId = arguments?.getInt(EXTRA_HISTORY_RUN_ID, 0)
        runSessionViewModel.runSessions.observe(viewLifecycleOwner) { sessions ->
            for (session in sessions) {
                if (session.sessionId == runSessionId) {
                    binding.historyRunDetail.textDistanceMetric.text =
                        getString(R.string.text_distance_metric, session.distance)
                    binding.historyRunDetail.textDurationMetric.text =
                        StatsUtils.formatDuration(session.duration!!)
                    userViewModel.fetchUserInfo()
                    userViewModel.userLiveData.observe(viewLifecycleOwner){
                        binding.historyRunProfile.textHistoryProfileName.text = it?.name
                        if(it?.metricPreference == User.UNIT_MILE){
                            binding.historyRunDetail.textPaceMetric.text =
                                getString(R.string.text_speed_metric_mile,session.speed)
                        }else{
                            binding.historyRunDetail.textPaceMetric.text =
                                getString(R.string.text_speed_metric,session.speed)
                        }
                    }
                    binding.historyRunDetail.textCalorieMetric.text =
                        getString(R.string.text_calorie_metric,session.caloriesBurned)
                    binding.historyRunProfile.textHistoryDetailDate.text = DateTimeUtils.formatDateHistoryDetailFormat(session.runDate)
                }
            }
        }
    }

    companion object {
        const val EXTRA_HISTORY_RUN_ID = "EXTRA_HISTORY_RUN_ID"
    }
}