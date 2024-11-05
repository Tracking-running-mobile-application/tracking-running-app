package com.app.java.trackingrunningapp.ui.training

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.app.java.trackingrunningapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TrainingPlans : Fragment() {

    private lateinit var imageViewPager: ViewPager2
    private lateinit var imagePagerAdapter: ImagePagerAdapter
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_training_plans, container, false)

        // Initialize ViewPager2 and adapter
        imageViewPager = view.findViewById(R.id.planImage)
        tabLayout = view.findViewById(R.id.tab_layout)

        val images = listOf(
            R.drawable.training_plan_image_1,
            R.drawable.training_plan_image_1,
            R.drawable.training_plan_image_1
        )

        imagePagerAdapter = ImagePagerAdapter(images)
        imageViewPager.adapter = imagePagerAdapter
        TabLayoutMediator(tabLayout, imageViewPager) { _, _ ->
            // Here you can configure each tab if needed
        }.attach()

        return view
    }
}
