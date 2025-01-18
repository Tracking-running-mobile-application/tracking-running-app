package com.app.java.trackingrunningapp.ui.home.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentRunPlanResultBinding


class RunPlanResultFragment : Fragment() {
    private lateinit var binding: FragmentRunPlanResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunPlanResultBinding.inflate(inflater,container,false)
        return binding.root
    }
}