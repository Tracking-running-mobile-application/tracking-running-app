package com.app.java.trackingrunningapp.ui.home.training

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.databinding.FragmentTrainingPlansBinding
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.TrainingPlanViewModelFactory
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class TrainingPlanFragment : Fragment() {
    private lateinit var binding: FragmentTrainingPlansBinding
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
        binding = FragmentTrainingPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupProgressBar()
        setUpView()
    }

    private fun setUpView() {
        val planTitle = arguments?.getString(EXTRA_TITLE_TRAINING_PLAN)!!
        val imageResId = arguments?.getInt(EXTRA_IMAGE_RES_ID,-1)!!
        // toolbar
        val toolbarTitle = arguments?.getString(EXTRA_PLAN_LEVEL) + "'s Guide"
        requireActivity().findViewById<TextView>(R.id.tv_toolbar_title).text = toolbarTitle
        setupImageSlider(imageResId)
        trainingPlanViewModel.recommendedPlansBeginner.observe(viewLifecycleOwner){beginnerPlans ->
            for (plan in beginnerPlans){
                if(plan.title.trim().compareTo(planTitle.trim()) == 0){
                    binding.planTitle.text = plan.title
                    binding.textPlanDescription.text = plan.description
                    binding.textPlanEstimateTime.text = getString(R.string.text_estimate_time,plan.estimatedTime)
                    binding.textPlanTargetDistance.text = getString(R.string.text_target_distance,plan.targetDistance)
                    binding.btnLiveStats.setOnClickListener {
                        // TODO:  
                    }
                }
            }
        }
        trainingPlanViewModel.recommendedPlansIntermediate.observe(viewLifecycleOwner){intermediatePlans ->
            for (plan in intermediatePlans){
                if(plan.title.trim().compareTo(planTitle.trim()) == 0){
                    binding.planTitle.text = plan.title
                    binding.textPlanDescription.text = plan.description
                    binding.textPlanEstimateTime.text = getString(R.string.text_estimate_time,plan.estimatedTime)
                    binding.textPlanTargetDistance.text = getString(R.string.text_target_distance,plan.targetDistance)
                    binding.btnLiveStats.setOnClickListener {
                        // TODO:  
                    }
                }
            }
        }
        trainingPlanViewModel.recommendedPlansAdvanced.observe(viewLifecycleOwner){advancedPlans ->
            for (plan in advancedPlans){
                if(plan.title.trim().compareTo(planTitle.trim()) == 0){
                    binding.planTitle.text = plan.title
                    binding.textPlanDescription.text = plan.description
                    binding.textPlanEstimateTime.text = getString(R.string.text_estimate_time,plan.estimatedTime)
                    binding.textPlanTargetDistance.text = getString(R.string.text_target_distance,plan.targetDistance)
                    binding.btnLiveStats.setOnClickListener {
//                        it.findNavController().navigate(R.id.action_trainingPlanFragment_to_nav_run)
                        // TODO:
                        requireActivity().findViewById<Button>(R.id.btn_start_tracking)
                        it.findNavController().navigate(R.id.action_trainingPlanFragment_to_nav_run)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupProgressBar() {
        var progressCounter = 0
        binding.btnStartTraining.setOnClickListener {
            setupProgressAction()
            progressCounter += 5
            if(progressCounter > 20) progressCounter = 20
            binding.progressBar.progress += progressCounter
            binding.tvProgressDesc.text = "You have run ${progressCounter}km! Keep it up!"
            // Tính phần trăm
            val percentage = (progressCounter.toDouble() / 20 * 100).toInt()
            binding.tvPercentage.text = "$percentage%"
        }
    }

    private fun setupProgressAction() {
        binding.btnCancelPlan.visibility = View.VISIBLE
        binding.btnLiveStats.visibility = View.VISIBLE
        binding.tvPercentage.visibility = View.VISIBLE
        binding.btnStartTraining.visibility = View.INVISIBLE
        // TODO: Do something with progress bar
        binding.btnCancelPlan.setOnClickListener {
            // reset progress
            binding.progressBar.progress = 0
//            binding.tvPercentage.visibility = View.INVISIBLE
            binding.btnCancelPlan.visibility = View.INVISIBLE
            binding.btnLiveStats.visibility = View.INVISIBLE
            binding.btnStartTraining.visibility = View.VISIBLE
        }
    }

    private fun setupImageSlider(imageResId: Int) {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(imageResId, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img05, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img13, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img28, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img23, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img12, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img27, ScaleTypes.FIT))
        binding.imageSlider.setImageList(imageList)
    }

    companion object {
        const val EXTRA_TITLE_TRAINING_PLAN = "EXTRA_TITLE_TRAINING_PLAN"
        const val EXTRA_IMAGE_RES_ID = "EXTRA_IMAGE_RES_ID"
        const val EXTRA_PLAN_LEVEL = "EXTRA_PLAN_LEVEL"
    }
}
