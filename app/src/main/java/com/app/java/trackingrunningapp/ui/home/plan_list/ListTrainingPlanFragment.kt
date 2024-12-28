package com.app.java.trackingrunningapp.ui.home.plan_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.dataclass.home.PlanExercise
import com.app.java.trackingrunningapp.databinding.FragmentListTrainingPlanBinding
import com.app.java.trackingrunningapp.ui.home.HomeFragment
import com.app.java.trackingrunningapp.ui.home.training.TrainingPlanFragment
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class ListTrainingPlanFragment : Fragment() {
    private lateinit var binding: FragmentListTrainingPlanBinding
    private lateinit var trainingPlanViewModel: TrainingPlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        binding = FragmentListTrainingPlanBinding.inflate(inflater, container, false)
        requireActivity().findViewById<TextView>(R.id.tv_toolbar_title).text = "Training Plans"
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).isVisible = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(EXTRA_TITLE_PLAN)
        if (title == "Beginner Run") {
            binding.textTitleExercise.text = "Beginner Run List"
            binding.imgPlanTitle.setImageResource(R.drawable.img_begginer_list)
            setUpBeginner()
        } else if (title == "Intermediate Run") {
            binding.imgPlanTitle.setImageResource(R.drawable.img_intermediate_list)
            binding.textTitleExercise.text = "Intermediate Run List"
            setUpIntermediate()

        } else if (title == "Advanced Run") {
            binding.imgPlanTitle.setImageResource(R.drawable.img_advance_list)
            binding.textTitleExercise.text = "Advanced Run List"
             setUpAdvance()
        }
    }

    private fun setUpAdvance() {
        val advancedExerciseList = listOf(
            PlanExercise("Advanced Distance Training","Running","Advanced",R.drawable.img21),
            PlanExercise("Marathon Preparation","Running","Advanced",R.drawable.img22),
            PlanExercise("Trail Mastery","Trail Running","Advanced",R.drawable.img23),
            PlanExercise("Endurance Running","Running","Advanced",R.drawable.img24),
            PlanExercise("Mindful Ultra Training","Mindful Running","Advanced",R.drawable.img25),
            PlanExercise("Trail Marathon Preparation","Trail Running","Advanced",R.drawable.img26),
            PlanExercise("Elite Hiking Endurance","Hiking","Advanced",R.drawable.img27),
            PlanExercise("Marathon Excellence","Running","Advanced",R.drawable.img28),
            PlanExercise("Trail Climbing Mastery","Trail Running","Advanced",R.drawable.img29),
            PlanExercise("Ultra Running Focus","Mindful Running","Advanced",R.drawable.img30),
        )
        binding.recyclerPlanBeginner.adapter = ListTrainingPlanAdapter(advancedExerciseList,
            object : ListTrainingPlanAdapter.OnItemExerciseClickListener {
                override fun onExerciseClick(exercise: PlanExercise) {
                    val bundle = Bundle().apply {
                        putString(TrainingPlanFragment.EXTRA_TITLE_TRAINING_PLAN,exercise.title)
                        putString(TrainingPlanFragment.EXTRA_PLAN_LEVEL,exercise.level)
                        putInt(TrainingPlanFragment.EXTRA_IMAGE_RES_ID,exercise.imageRes)
                    }
                    findNavController().navigate(R.id.action_listTrainingPlanFragment_to_trainingPlanFragment,bundle)
                }
            })
    }
    private fun setUpIntermediate() {
        val intermediateExerciseList = listOf(
            PlanExercise("Distance Builder","Running","Intermediate",R.drawable.img11),
            PlanExercise("Trail Endurance Plan","Trail Running","Intermediate",R.drawable.img12),
            PlanExercise("Stamina for Long Runs","Running","Intermediate",R.drawable.img13),
            PlanExercise("Trail Fitness Plan","Trail Running","Intermediate",R.drawable.img14),
            PlanExercise("Long Distance Focus","Running","Intermediate",R.drawable.img15),
            PlanExercise("Mindful Endurance","Mindful Running","Intermediate",R.drawable.img16),
            PlanExercise("Advanced Trail Exploration","Trail Running","Intermediate",R.drawable.img17),
            PlanExercise("Trail Climbing Strength","Trail Running","Intermediate",R.drawable.img18),
            PlanExercise("Focused Running","Running","Intermediate",R.drawable.img19),
            PlanExercise("Trail Endurance Challenge","Trail Running","Intermediate",R.drawable.img20),
        )
        binding.recyclerPlanBeginner.adapter = ListTrainingPlanAdapter(intermediateExerciseList,
            object : ListTrainingPlanAdapter.OnItemExerciseClickListener {
                override fun onExerciseClick(exercise: PlanExercise) {
                    val bundle = Bundle().apply {
                        putString(TrainingPlanFragment.EXTRA_TITLE_TRAINING_PLAN,exercise.title)
                        putString(TrainingPlanFragment.EXTRA_PLAN_LEVEL,exercise.level)
                        putInt(TrainingPlanFragment.EXTRA_IMAGE_RES_ID,exercise.imageRes)
                    }
                    findNavController().navigate(R.id.action_listTrainingPlanFragment_to_trainingPlanFragment,bundle)
                }
            })
    }

    private fun setUpBeginner() {
        val beginnerExerciseList = listOf(
            PlanExercise("Running for Beginners", "Running", "Beginner", R.drawable.img01),
            PlanExercise("Trail Hiking Basics", "Hiking", "Beginner", R.drawable.img02),
            PlanExercise("Mindful Movement", "Mindful Running", "Beginner", R.drawable.img03),
            PlanExercise(
                "Strength Training for Trails",
                "Trail Running",
                "Beginner",
                R.drawable.img04
            ),
            PlanExercise("Trail Exploration Start", "Trail Running", "Beginner", R.drawable.img05),
            PlanExercise("Easy Running Plan", "Running", "Beginner", R.drawable.img06),
            PlanExercise("Hiking Preparation", "Hiking", "Beginner", R.drawable.img07),
            PlanExercise("Introduction to Running", "Running", "Beginner", R.drawable.img08),
            PlanExercise("Trail Basics", "Trail", "Beginner", R.drawable.img09),
            PlanExercise("Mindful Jogging", "Jogging", "Beginner", R.drawable.img10)
        )
        binding.recyclerPlanBeginner.adapter = ListTrainingPlanAdapter(beginnerExerciseList,
            object : ListTrainingPlanAdapter.OnItemExerciseClickListener {
                override fun onExerciseClick(exercise: PlanExercise) {
                    val bundle = Bundle().apply {
                        putString(TrainingPlanFragment.EXTRA_TITLE_TRAINING_PLAN,exercise.title)
                        putString(TrainingPlanFragment.EXTRA_PLAN_LEVEL,exercise.level)
                        putInt(TrainingPlanFragment.EXTRA_IMAGE_RES_ID,exercise.imageRes)
                    }
                    findNavController().navigate(R.id.action_listTrainingPlanFragment_to_trainingPlanFragment,bundle)
                }
            })
    }
    companion object {
        const val EXTRA_TITLE_PLAN = "EXTRA_TITLE_PLAN"
    }
}