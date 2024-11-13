package com.app.java.trackingrunningapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.DailyTask
import com.app.java.trackingrunningapp.data.model.TrainingPlan
import com.app.java.trackingrunningapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create a list of TrainingPlan objects
        val trainingPlans = listOf(
            TrainingPlan("Beginner Run", R.drawable.img_beginner),
            TrainingPlan("Intermediate Run", R.drawable.img_intermediate),
            TrainingPlan("Advanced Run", R.drawable.img_advanced)
        )

        // Set up the RecyclerView with a LinearLayoutManager and the adapter
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewTrainingPlans.layoutManager = layoutManager
        binding.recyclerViewTrainingPlans.adapter = TrainingPlansAdapter(trainingPlans)

        // Create a list of DailyTask objects
        val dailyTasks = listOf(
            DailyTask("Beginner Run", "20 minutes", "Daily", R.drawable.img_beginner, isChecked = true),
            DailyTask("My Run 1", "40 minutes", "Monday, Wednesday, Friday", R.drawable.img_intermediate, isChecked = false),
            DailyTask("My Run 2", "10 minutes", "Daily", R.drawable.img_advanced, isChecked = true),
            DailyTask("My Run 3", "30 minutes", "Daily", R.drawable.img_advanced, isChecked = true)
        )

        // Set up the RecyclerView for Daily Tasks
        val dailyTasksLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDailyTasks.layoutManager = dailyTasksLayoutManager
        binding.recyclerViewDailyTasks.adapter = DailyTasksAdapter(dailyTasks)
        // test onclick
        binding.planName.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_trainingPlans)
            requireActivity().findViewById<TextView>(R.id.tv_toolbar_title).text = getString(R.string.training_plans)
        }
        // test onclick
        binding.addDailyTaskIcon.setOnClickListener{
            it.findNavController().navigate(R.id.action_homeFragment_to_dailyTask)
            requireActivity().findViewById<TextView>(R.id.tv_toolbar_title).text = getString(R.string.daily_tasks)
        }

        //
//        binding.avatarImage.setOnClickListener {
//            it.findNavController().navigate(R.id.action_homeFragment_to_homeFragmentScroll)
//        }
    }
}