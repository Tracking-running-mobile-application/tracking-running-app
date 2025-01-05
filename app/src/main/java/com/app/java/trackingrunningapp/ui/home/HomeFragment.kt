package com.app.java.trackingrunningapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.dataclass.home.PersonalGoal
import com.app.java.trackingrunningapp.data.model.dataclass.home.TrainingPlan
import com.app.java.trackingrunningapp.databinding.FragmentHomeBinding
import com.app.java.trackingrunningapp.ui.home.personalGoal.PersonalGoalAdapter
import com.app.java.trackingrunningapp.ui.home.plan_list.ListTrainingPlanFragment
import com.app.java.trackingrunningapp.ui.viewmodel.PersonalGoalViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.PersonalGoalViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var trainingPlanViewModel: TrainingPlanViewModel
    private lateinit var personalGoalViewModel: PersonalGoalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Do nothing to disable back button
            }
        })
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val planFactory = TrainingPlanViewModelFactory(
            InitDatabase.trainingPlanRepository,
            InitDatabase.notificationRepository,
            InitDatabase.runSessionRepository
        )
        trainingPlanViewModel =
            ViewModelProvider(this, planFactory).get(TrainingPlanViewModel::class.java)

        // fetch data
        trainingPlanViewModel.fetchRecommendedPlans()
        // init personal goal viewmodel
        val personalGoalViewModelFactory = PersonalGoalViewModelFactory(InitDatabase.personalGoalRepository,InitDatabase.runSessionRepository)
        personalGoalViewModel = ViewModelProvider(requireActivity(),personalGoalViewModelFactory)[PersonalGoalViewModel::class.java]

        personalGoalViewModel.loadPersonalGoals()
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTrainingPlanRecycler()
        setUpPersonalGoal()
    }

    private fun setUpPersonalGoal() {
        // Create a list of DailyTask objects
        val dailyTasks = listOf(
            PersonalGoal(
                "Beginner Run", "20 minutes", "Daily",
                R.drawable.img_beginner
            ),
            PersonalGoal(
                "My Run 1", "40 minutes", "Monday, Wednesday, Friday",
                R.drawable.img_intermediate
            ),
            PersonalGoal(
                "My Run 2", "10 minutes", "Daily",
                R.drawable.img_advanced
            ),
            PersonalGoal(
                "My Run 3", "30 minutes", "Daily",
                R.drawable.img_advanced
            )
        )
        binding.rvDailyTask.adapter = PersonalGoalAdapter(dailyTasks,
            object : PersonalGoalAdapter.OnItemPersonalGoalListener {
                override fun onClick(dailyTask: PersonalGoal) {
                    dailyTask.isClicked++
                }
            })
        binding.icAddGoal.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_personalGoalFragment)
        }
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

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // restore to default behavior
                isEnabled = false // disable this callback
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        })
    }
}