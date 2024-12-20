package com.app.java.trackingrunningapp.ui.home.training

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        val planFactory = TrainingPlanViewModelFactory(InitDatabase.trainingPlanRepository, InitDatabase.notificationRepository, InitDatabase.runSessionRepository)
        trainingPlanViewModel = ViewModelProvider(this, planFactory).get(TrainingPlanViewModel::class.java)

        // fetch data
        trainingPlanViewModel.fetchRecommendedPlans()

        binding = FragmentTrainingPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val resId = R.drawable.img04
        Log.d("res_id","$resId")
        setupViewModel()
        setupImageSlider()
        setupProgressBar()
    }

    @SuppressLint("SetTextI18n")
    private fun setupViewModel() {
        val title = arguments?.getString(EXTRA_TITLE_TRAINING_PLAN)
        if(title == "Beginner Run"){
            // beginner
            trainingPlanViewModel.recommendedPlansBeginner.observe(viewLifecycleOwner) {
                    trainingPlans ->
                if (trainingPlans.isNotEmpty()) {
                    for (plan in trainingPlans) {
                        Log.d(
                            "Training Plan Log",
                            "${plan.planId}, ${plan.imagePath}, ${plan.title}, ${plan.description}, ${plan.difficulty}"
                        )
                        binding.planTitle.text = plan.title
                        binding.textPlanDescription.text = plan.description
                        binding.textPlanEstimateTime.text = plan.estimatedTime.toInt().toString() + " minute"
                        binding.textPlanTargetDistance.text =plan.targetDistance?.toInt().toString() + " km"
                        return@observe
                    }
                } else {
                    Log.e("Error LOL", "No training plan exist re-install the app or clean project before call me please")
                }
            }
        }else if(title == "Intermediate Run"){
            // intermediate
            trainingPlanViewModel.recommendedPlansIntermediate.observe(viewLifecycleOwner) {
                    trainingPlans ->
                if (trainingPlans.isNotEmpty()) {
                    for (plan in trainingPlans) {
                        Log.d(
                            "Training Plan Log",
                            "${plan.planId}, ${plan.imagePath}, ${plan.title}, ${plan.description}, ${plan.difficulty}"
                        )
                    }
                } else {
                    Log.e("Error LOL", "No training plan exist re-install the app or clean project before call me please")
                }
            }
        } else if(title == "Advanced Run"){
            // advanced
            trainingPlanViewModel.recommendedPlansAdvanced.observe(viewLifecycleOwner) {
                    trainingPlans ->
                if (trainingPlans.isNotEmpty()) {
                    for (plan in trainingPlans) {
                        Log.d(
                            "Training Plan Log",
                            "${plan.planId}, ${plan.imagePath}, ${plan.title}, ${plan.description}, ${plan.difficulty}"
                        )
                    }
                } else {
                    Log.e("Error LOL", "No training plan exist re-install the app or clean project before call me please")
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

            // Di chuyển TextView theo tiến trình
            binding.progressBar.post {
                val progressBarWidth = binding.progressBar.width
                val progressBarPadding = binding.progressBar.paddingLeft + binding.progressBar.paddingRight
                val actualWidth = progressBarWidth - progressBarPadding // Chiều rộng thực

                val progressPosition = (actualWidth * progressCounter / 20)
                val textWidth = binding.tvPercentage.width
                val offset = progressPosition - (textWidth / 2) + 48
                binding.tvPercentage.translationX = offset.toFloat()
            }

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
            binding.tvPercentage.visibility = View.INVISIBLE
            binding.btnCancelPlan.visibility = View.INVISIBLE
            binding.btnLiveStats.visibility = View.INVISIBLE
            binding.btnStartTraining.visibility = View.VISIBLE
        }
    }

    private fun setupImageSlider() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.img_running_man, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.img_advanced, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.img_intermediate, ScaleTypes.CENTER_CROP))
        binding.imageSlider.setImageList(imageList)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility =
            View.VISIBLE
    }

    companion object {
        const val EXTRA_TITLE_TRAINING_PLAN = "EXTRA_TITLE_TRAINING_PLAN"
    }
}
