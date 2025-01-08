package com.app.java.trackingrunningapp.ui.run

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.databinding.FragmentRunResultBinding
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.RunSessionViewModelFactory
import com.app.java.trackingrunningapp.utils.StatsUtils
import com.google.android.material.bottomnavigation.BottomNavigationView


class RunResultFragment : Fragment() {
    private lateinit var binding:FragmentRunResultBinding
    private lateinit var runSessionViewModel: RunSessionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunResultBinding.inflate(inflater,container,false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE
        val runFactory = RunSessionViewModelFactory(InitDatabase.runSessionRepository)
        runSessionViewModel =
            ViewModelProvider(this, runFactory).get(RunSessionViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setUpToolbar()
        setUpAction()
    }

    private fun setupView() {
        val runSessionId = arguments?.getInt(EXTRA_RUN_ID_RESULT,0)
        Log.d("RunIdResult","$runSessionId")
        runSessionViewModel.runSessions.observe(viewLifecycleOwner) { sessions ->
            for (session in sessions) {
                if (session.sessionId == runSessionId) {
                    binding.layoutResult.textDistanceMetric.text =
                        getString(R.string.text_distance_metric, session.distance)
                    binding.layoutResult.textDurationMetric.text =
                        StatsUtils.formatDuration(session.duration!!)
                    binding.layoutResult.textPaceMetric.text =
                        getString(R.string.text_pace_metric,session.pace)
                    binding.layoutResult.textCalorieMetric.text =
                        getString(R.string.text_calorie_metric,session.caloriesBurned)
                }
            }
        }
    }

    private fun setUpAction() {
        binding.btnRunConfirm.setOnClickListener {
            it.findNavController().navigate(R.id.action_global_runFragment)
            Toast.makeText(requireContext(),"Added Failure",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpToolbar() {
        val toolbarTitle = requireActivity().findViewById<TextView>(R.id.tv_toolbar_title)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_main)
        toolbar.navigationIcon = null
        toolbarTitle.text = "Run Result"
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.VISIBLE
    }

    companion object{
        const val EXTRA_RUN_ID_RESULT = "EXTRA_RUN_ID_RESULT"
    }
}