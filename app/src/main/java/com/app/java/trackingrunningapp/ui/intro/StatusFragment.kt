package com.app.java.trackingrunningapp.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentStatusBinding
import com.app.java.trackingrunningapp.ui.MainActivity
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
import kotlinx.coroutines.launch

class StatusFragment : Fragment() {
    private lateinit var binding: FragmentStatusBinding
    private var isFtClicked = false
    private var isLbsClicked = false
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatusBinding.inflate(inflater, container, false)
        val userRepository = UserRepository()
        val userFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToggle()
        setupConfirm()
    }

    private fun setupConfirm() {
        binding.btnConfirm.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            val userName = binding.edtName.text.toString()
            val ageStr = binding.edtAge.text.toString()
            val heightStr = binding.edtHeight.text.toString()
            val weightStr = binding.edtWeight.text.toString()
            
            val age = ageStr.toIntOrNull()
            var userHeight = heightStr.toFloatOrNull()
            var userWeight = weightStr.toDoubleOrNull()
            if (isFtClicked) {
                val heightFt = heightStr.toFloatOrNull()
                userHeight = (heightFt?.times(30.48))?.toFloat()
            }
            if (isLbsClicked) {
                val weightLbs = weightStr.toDoubleOrNull()
                userWeight = weightLbs?.times(0.453592)
            }
            if (userHeight == 0.0f || userWeight == 0.0) {
                Toast.makeText(
                    requireContext(),
                    "Invalid height or weight entered",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                lifecycleScope.launch {
                    var metric = User.KILOGRAM
                    var unit = "cm"
                    if(isLbsClicked){
                        metric = User.POUNDS
                    }
                    if(isFtClicked){
                        unit = "ft"
                    }
                    userViewModel.upsertUserInfo(
                        name = userName,
                        age = age,
                        height = userHeight,
                        weight = userWeight ?: 50.0,
                        metricPreference = metric,
                        unit = unit
                    )
                }
            }
            startActivity(intent)
        }
    }

    private fun setUpToggle() {
        val btnFt = binding.btnFt
        val btnCm = binding.btnCm
        val btnKg = binding.btnKg
        val btnLbs = binding.btnLbs
        val hintHeight = binding.textHintHeightUnit
        val hintWeight = binding.textHintWeightUnit


        //ft
        btnFt.setOnClickListener {
            hintHeight.text = getString(R.string.text_ft)
            isFtClicked = true
            btnFt.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
        //cm
        btnCm.setOnClickListener {
            hintHeight.text = getString(R.string.text_cm)
            isFtClicked = false
            btnFt.setBackgroundColor(requireContext().getColor(R.color.main_gray))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
        }
        //kg
        btnKg.setOnClickListener {
            isLbsClicked = false
            hintWeight.text = getString(R.string.text_kg)
            btnKg.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnLbs.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
        //lbs
        btnLbs.setOnClickListener {
            isLbsClicked = true
            hintWeight.text = getString(R.string.text_lbs)
            btnLbs.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnKg.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
    }
}