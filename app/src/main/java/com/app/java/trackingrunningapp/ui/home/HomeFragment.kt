package com.app.java.trackingrunningapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.dataclass.home.DailyTask
import com.app.java.trackingrunningapp.data.model.dataclass.home.TrainingPlan
import com.app.java.trackingrunningapp.databinding.FragmentHomeBinding
import com.app.java.trackingrunningapp.ui.home.plan_list.ListTrainingPlanFragment
import com.app.java.trackingrunningapp.ui.home.training.DailyTasksAdapter
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var trainingPlanViewModel: TrainingPlanViewModel
    private  val trainingPlans = mutableListOf<TrainingPlan>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val planFactory = TrainingPlanViewModelFactory(InitDatabase.trainingPlanRepository, InitDatabase.notificationRepository, InitDatabase.runSessionRepository)
        trainingPlanViewModel = ViewModelProvider(this, planFactory).get(TrainingPlanViewModel::class.java)

        // fetch data
        trainingPlanViewModel.fetchRecommendedPlans()
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTrainingPlanRecycler()
        setUpDailyTaskRecycler()
    }

    private fun setUpDailyTaskRecycler() {
        // Create a list of DailyTask objects
        val dailyTasks = listOf(
            DailyTask(
                "Beginner Run", "20 minutes", "Daily",
                R.drawable.img_beginner
            ),
            DailyTask(
                "My Run 1", "40 minutes", "Monday, Wednesday, Friday",
                R.drawable.img_intermediate
            ),
            DailyTask(
                "My Run 2", "10 minutes", "Daily",
                R.drawable.img_advanced
            ),
            DailyTask(
                "My Run 3", "30 minutes", "Daily",
                R.drawable.img_advanced
            )
        )

        binding.rvDailyTask.adapter = DailyTasksAdapter(dailyTasks,
            object : DailyTasksAdapter.OnItemDailyTaskListener {
                override fun onClick(dailyTask: DailyTask) {
                    dailyTask.isClicked++
                }
            })
    }

    private fun setUpTrainingPlanRecycler() {
        val trainingPlans = listOf(
            TrainingPlan("Beginner Run", R.drawable.img_beginner),
            TrainingPlan("Intermediate Run", R.drawable.img_intermediate),
            TrainingPlan("Advanced Run", R.drawable.img_advanced)
        )
//        val trainingPlans = mutableListOf<TrainingPlan>()
        // beginner
        binding.rvTrainingPlans.adapter = HomeTrainingPlanAdapter(trainingPlans,
            object : HomeTrainingPlanAdapter.OnItemTrainingClickListener {
                override fun onClick(trainingPlan: TrainingPlan) {
                    val bundle = Bundle().apply {
                        this.putString(
                            ListTrainingPlanFragment.EXTRA_TITLE_PLAN,
                            trainingPlan.name
                        )
                    }
                    findNavController()
                        .navigate(R.id.action_homeFragment_to_listTrainingPlanFragment, bundle)
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
                        .visibility = View.GONE
                }
            })
    }

    override fun onStop() {
        super.onStop()
        val toolbar = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main)
        // hide setting
        val itemSetting = toolbar.menu.findItem(R.id.item_toolbar_setting)
        itemSetting.isVisible = false
    }
}