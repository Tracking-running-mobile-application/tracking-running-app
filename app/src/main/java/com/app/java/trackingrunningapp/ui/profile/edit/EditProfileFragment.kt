package com.app.java.trackingrunningapp.ui.profile.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentEditProfileBinding
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var user: User
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
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        requireActivity().findViewById<MaterialToolbar>(R.id.toolbar_main).menu.findItem(R.id.item_toolbar_setting).isVisible =
            false
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = User(userId = 1)
        setUpToggle()
        requireActivity().findViewById<TextView>(R.id.tv_toolbar_title).text =
            getString(R.string.text_edit_profile)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE

        userViewModel.fetchUserInfo()
        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            Log.d("Edit Profile Fragment", "$user")
            binding.edtName.setText(user?.name.toString())
            binding.edtAge.setText(user?.age.toString())
            binding.edtHeight.setText(getString(R.string.profile_height, user?.height))
            binding.edtWeight.setText(getString(R.string.edit_weight, user?.weight))
            if (user?.metricPreference == User.POUNDS) {
                binding.btnLbs.performClick()
            }
            if (user?.unit == "ft") {
                binding.btnFt.performClick()
            }
        }
        binding.btnSave.setOnClickListener {
            val userName = binding.edtName.text.toString()
            val ageStr = binding.edtAge.text.toString()
            val heightStr = binding.edtHeight.text.toString()
            val weightStr = binding.edtWeight.text.toString()

            val age = ageStr.toIntOrNull()
            user.name = userName
            user.age = age
            var userHeight = heightStr.toFloatOrNull()
            var userWeight = weightStr.toDoubleOrNull()
            if (userWeight != null && userHeight != null) {
                if (isFtClicked) {
                    val heightFt = heightStr.toFloatOrNull()
//                    userHeight = (heightFt?.times(30.48))?.toFloat()
                    user.height = heightFt
                    user.unit = "ft"
                } else {
                    user.height = userHeight
                    user.unit = "cm"
                }

                if (isLbsClicked) {
                    val weightLbs = weightStr.toDoubleOrNull()
//                userWeight = weightLbs?.times(0.453592)
                    user.weight = weightLbs
                    user.metricPreference = User.POUNDS
                } else {
                    user.weight = userWeight
                    user.metricPreference = User.KILOGRAM
                }
                lifecycleScope.launch {
                    userViewModel.upsertUserInfo(
                        name = user.name,
                        age = user.age,
                        height = user.height,
                        weight = user.weight ?: 50.0,
                        metricPreference = user.metricPreference,
                        unit = user.unit
                    )
                }
            } else {
                if (isFtClicked) {
                    val heightFt = heightStr.toFloatOrNull()
//                    userHeight = (heightFt?.times(30.48))?.toFloat()
                    user.height = heightFt
                    user.unit = "ft"
                } else {
                    user.height = userHeight
                    user.unit = "cm"
                }

                if (isLbsClicked) {
                    val weightLbs = weightStr.toDoubleOrNull()
//                userWeight = weightLbs?.times(0.453592)
                    user.weight = weightLbs
                    user.metricPreference = User.POUNDS
                } else {
                    user.weight = userWeight
                    user.metricPreference = User.KILOGRAM
                }
//                userViewModel.fetchUserInfo()
                Log.d("user_pound", "$user.metricPreference")
                userViewModel.userLiveData.observe(viewLifecycleOwner) {
                    Log.d("userrrrr", "$user")
                    if (userHeight != null) {
                        lifecycleScope.launch {
                            userViewModel.upsertUserInfo(
                                name = it?.name,
                                age = it?.age,
                                height = userHeight,
                                weight = it?.weight ?: 50.0,
                                metricPreference = user.metricPreference,
                                unit = user.unit
                            )
                        }
                    }
                    if (userWeight != null) {
                        lifecycleScope.launch {
                            userViewModel.upsertUserInfo(
                                name = it?.name,
                                age = it?.age,
                                height = it?.height,
                                weight = userWeight,
                                metricPreference = user.metricPreference,
                                unit = user.unit
                            )
                        }
                    }
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
            user.unit = "ft"
            btnFt.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
        //cm
        btnCm.setOnClickListener {
            isFtClicked = false
            hintHeight.text = getString(R.string.text_cm)
            user.unit = "cm"
            binding.edtHeight.setText("")
            btnFt.setBackgroundColor(requireContext().getColor(R.color.main_gray))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
        }
        //kg
        btnKg.setOnClickListener {
            isLbsClicked = false
            hintWeight.text = getString(R.string.text_kg)
            user.metricPreference = User.KILOGRAM
            binding.edtWeight.setText("")
            btnKg.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnLbs.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
        //lbs
        btnLbs.setOnClickListener {
            isLbsClicked = true
            hintWeight.text = getString(R.string.text_lbs)
            user.metricPreference = User.POUNDS
            btnLbs.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnKg.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility =
            View.VISIBLE
    }
}