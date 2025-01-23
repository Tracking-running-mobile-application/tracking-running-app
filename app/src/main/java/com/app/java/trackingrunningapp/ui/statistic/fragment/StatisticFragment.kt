package com.app.java.trackingrunningapp.ui.statistic.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentStatisticBinding
import com.app.java.trackingrunningapp.ui.statistic.adapter.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class StatisticFragment : Fragment() {
    private lateinit var binding: FragmentStatisticBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPager2()
    }

    private fun initPager2() {
        val pagerAdapter = PagerAdapter(this)
        binding.pagerStatistic.adapter = pagerAdapter
        val titles = listOf(getString(R.string.weekly), getString(R.string.monthly),
            getString(R.string.yearly))
        TabLayoutMediator(binding.tabLayoutStatistic, binding.pagerStatistic)
        { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    override fun onStop() {
        super.onStop()
        // pop to profile
        this.findNavController().popBackStack(R.id.profileFragment,false)
    }
}