package com.app.java.trackingrunningapp.ui.profile.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentEditProfileBinding
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class EditProfileFragment:Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    private var isFtClicked = false
    private var isLbsClicked = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userRepository = UserRepository()
        val userFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]
        binding = FragmentEditProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToggle()
        requireActivity().findViewById<TextView>(R.id.tv_toolbar_title).text = "Edit Profile"
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE
        userViewModel.fetchUserInfo()
        userViewModel.userLiveData.observe(viewLifecycleOwner){user->
            Log.d("Edit Profile Fragment", "$user")
            binding.edtName.setText(user?.name.toString())
            binding.edtAge.setText(user?.age.toString())
            binding.edtHeight.setText(getString(R.string.profile_height,user?.height))
            binding.edtWeight.setText(getString(R.string.edit_weight,user?.weight))
        }

        binding.btnSave.setOnClickListener {
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
                    userViewModel.upsertUserInfo(
                        name = userName,
                        age = age,
                        height = userHeight,
                        weight = userWeight ?: 50.0,
                        metricPreference = "km",
                        unit = "kg"
                    )
                }
            }
            requireActivity().onBackPressedDispatcher.onBackPressed()
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
            isFtClicked = true
            hintHeight.text = getString(R.string.text_ft)
            btnFt.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
        //cm
        btnCm.setOnClickListener {
            isFtClicked = false
            hintHeight.text = getString(R.string.text_cm)
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

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.VISIBLE
    }
}