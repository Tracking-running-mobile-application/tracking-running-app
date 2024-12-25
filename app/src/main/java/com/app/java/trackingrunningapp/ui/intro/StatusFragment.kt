package com.app.java.trackingrunningapp.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.database.RunningDatabase
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentStatusBinding
import com.app.java.trackingrunningapp.ui.MainActivity
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
import kotlinx.coroutines.launch

class StatusFragment : Fragment() {
    private lateinit var binding: FragmentStatusBinding
    private lateinit var userViewModel: UserViewModel

//    private val viewModel = UserViewModel(UserRepository())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatusBinding.inflate(inflater, container, false)

        val userFactory = UserViewModelFactory(InitDatabase.userRepository)
        userViewModel = ViewModelProvider(this,userFactory)[UserViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToggle()
        setupConfirm()

    }

    private fun setupConfirm() {
        binding.btnConfirm.setOnClickListener {
            val intent = Intent(requireContext(),MainActivity::class.java)
            val ageStr = binding.edtAge.text.toString()
            var age:Int = 1
            if(ageStr.isNotEmpty()){
                age = ageStr.toInt()
            }
            val height = binding.edtHeight.text.toString().toFloat()
            val weight = binding.edtWeight.text.toString().toDouble()
            lifecycleScope.launch {
                userViewModel.upsertUserInfo(
                    name = "Thang",
                    age = age,
                    height = height,
                    weight = weight,
                    metricPreference = "kg",
                    unit = "km"
                )
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
            btnFt.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
        //cm
        btnCm.setOnClickListener {
            hintHeight.text = getString(R.string.text_cm)
            btnFt.setBackgroundColor(requireContext().getColor(R.color.main_gray))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
        }
        //kg
        btnKg.setOnClickListener {
            hintWeight.text = getString(R.string.text_kg)
            btnKg.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnLbs.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
        //lbs
        btnLbs.setOnClickListener {
            hintWeight.text = getString(R.string.text_lbs)
            btnLbs.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnKg.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }

    }
}