package com.app.java.trackingrunningapp.ui.home

import android.os.Bundle
import android.util.Log
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
import com.app.java.trackingrunningapp.data.model.dataclass.home.TrainingPlan
import com.app.java.trackingrunningapp.data.model.entity.PersonalGoal
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentHomeBinding
import com.app.java.trackingrunningapp.ui.home.personalGoal.PersonalGoalAdapter
import com.app.java.trackingrunningapp.ui.home.plan_list.ListTrainingPlanFragment
import com.app.java.trackingrunningapp.ui.viewmodel.PersonalGoalViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.PersonalGoalViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModelFactory
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var trainingPlanViewModel: TrainingPlanViewModel
    private lateinit var personalGoalViewModel: PersonalGoalViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
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
        val personalGoalViewModelFactory = PersonalGoalViewModelFactory(
            InitDatabase.personalGoalRepository,
            InitDatabase.runSessionRepository
        )
        personalGoalViewModel = ViewModelProvider(
            requireActivity(),
            personalGoalViewModelFactory
        )[PersonalGoalViewModel::class.java]
        personalGoalViewModel.loadPersonalGoals()
        // user viewmodel
        val userFactory = UserViewModelFactory(UserRepository())
        userViewModel = ViewModelProvider(requireActivity(), userFactory)[UserViewModel::class.java]
        userViewModel.fetchUserInfo()
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.userLiveData.observe(viewLifecycleOwner){
            Log.d("user_home","$it")
            binding.textHomeUserName.text = getString(R.string.user_name,it?.name)
        }
        setUpTrainingPlanRecycler()
        setUpPersonalGoal()
    }

    private fun setUpPersonalGoal() {
        binding.icAddGoal.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_personalGoalFragment)
        }
        // Create a list of DailyTask objects
        personalGoalViewModel.personalGoalsLiveData.observe(viewLifecycleOwner) { goals ->
            if (goals.isNotEmpty()) {
                binding.imgNoGoal.visibility = View.GONE
                binding.rvPersonalGoal.visibility = View.VISIBLE
            }
            val personalGoals = mutableListOf<PersonalGoal>()
            for (goal in goals) {
                val personalGoal = PersonalGoal(
                    name = goal.name,
                    targetDistance = goal.targetDistance,
                    targetDuration = goal.targetDuration,
                    targetCaloriesBurned = goal.targetCaloriesBurned
                )
                personalGoals.add(personalGoal)
            }
            binding.rvPersonalGoal.adapter = PersonalGoalAdapter(personalGoals, requireContext(),
                object : PersonalGoalAdapter.OnItemPersonalGoalListener {
                    override fun onClick(personalGoal: PersonalGoal) {
                        TODO("Not yet implemented")
                    }
                }
            )
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

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // restore to default behavior
                    isEnabled = false // disable this callback
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            })
    }
}