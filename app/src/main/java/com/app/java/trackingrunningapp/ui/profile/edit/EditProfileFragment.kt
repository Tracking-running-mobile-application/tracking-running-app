package com.app.java.trackingrunningapp.ui.profile.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.databinding.FragmentEditProfileBinding
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class EditProfileFragment:Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater,container,false)
        val userFactory = UserViewModelFactory(InitDatabase.userRepository)
        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]

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
            binding.edtAge.setText(user?.age.toString())
            binding.edtHeight.setText(user?.height.toString())
            binding.edtWeight.setText(user?.weight.toString())
        }

        binding.btnSave.setOnClickListener {
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

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.VISIBLE
    }
}