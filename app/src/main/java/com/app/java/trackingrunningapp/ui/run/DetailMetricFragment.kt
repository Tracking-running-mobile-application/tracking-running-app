package com.app.java.trackingrunningapp.ui.run

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.java.trackingrunningapp.databinding.ItemHistoryRunDetailBinding

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
}