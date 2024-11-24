package com.app.java.trackingrunningapp.ui.home.training

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        binding = FragmentTrainingPlansBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupImageSlider()
        setupProgressBar()
    }

    private fun setupProgressBar() {
        var progressCounter = 0
        binding.btnStartTraining.setOnClickListener {
            setupProgressAction()
            progressCounter += 5
            binding.progressBar.progress += progressCounter
            binding.tvStartProgress.text = progressCounter.toString()
            binding.tvProgressDesc.text = "You have run ${progressCounter}km! Keep it up!"
        }
    }

    private fun setupProgressAction() {
        binding.btnCancelPlan.visibility = View.VISIBLE
        binding.btnLiveStats.visibility = View.VISIBLE
        binding.btnStartTraining.visibility = View.INVISIBLE
        // TODO: Do something with progress bar
        binding.btnCancelPlan.setOnClickListener {
            // reset progress
            binding.progressBar.progress = 0
            binding.tvStartProgress.text = "0"
            binding.btnCancelPlan.visibility = View.INVISIBLE
            binding.btnLiveStats.visibility = View.INVISIBLE
            binding.btnStartTraining.visibility = View.VISIBLE
        }
    }

    private fun setupImageSlider() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.img_running_man,ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.img_advanced,ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(R.drawable.img_intermediate,ScaleTypes.CENTER_CROP))
        binding.imageSlider.setImageList(imageList)
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.VISIBLE
    }
}
