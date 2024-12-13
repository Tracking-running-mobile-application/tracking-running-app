package com.app.java.trackingrunningapp.ui.home.training

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentTrainingPlansBinding
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class TrainingPlanFragment : Fragment() {
    private lateinit var binding: FragmentTrainingPlansBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrainingPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(EXTRA_TITLE_TRAINING_PLAN)
        binding.planTitle.text = title
        setupImageSlider()
        setupProgressBar()
        // navigate to setting
        requireActivity().findViewById<Toolbar>(R.id.toolbar_main).menu.findItem(R.id.item_toolbar_setting)
            .setOnMenuItemClickListener {
                findNavController().navigate(R.id.action_trainingPlans_to_settingFragment2)
                true
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
