package com.app.java.trackingrunningapp.ui.run

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.ItemHistoryRunDetailBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailMetricFragment:Fragment() {
    private lateinit var binding: ItemHistoryRunDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemHistoryRunDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
    }
}