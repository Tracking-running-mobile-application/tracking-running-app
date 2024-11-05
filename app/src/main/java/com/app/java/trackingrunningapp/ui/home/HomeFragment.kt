package com.app.java.trackingrunningapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentHomeBinding
import com.app.java.trackingrunningapp.databinding.FragmentIntroBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
    }
}