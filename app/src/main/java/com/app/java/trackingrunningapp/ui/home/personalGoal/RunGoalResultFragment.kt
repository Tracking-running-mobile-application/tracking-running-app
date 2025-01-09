package com.app.java.trackingrunningapp.ui.home.personalGoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentRunGoalResultBinding
import com.app.java.trackingrunningapp.databinding.FragmentRunPlanResultBinding


class RunGoalResultFragment : Fragment() {
    private lateinit var binding: FragmentRunGoalResultBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunGoalResultBinding.inflate(inflater,container,false)
        return binding.root
    }
}