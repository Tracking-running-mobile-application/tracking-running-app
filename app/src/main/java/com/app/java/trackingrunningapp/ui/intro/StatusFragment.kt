package com.app.java.trackingrunningapp.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.RunningDatabase
import com.app.java.trackingrunningapp.data.model.entity.user.User
import com.app.java.trackingrunningapp.data.repository.StatsRepository
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentStatusBinding
import com.app.java.trackingrunningapp.ui.MainActivity
import com.app.java.trackingrunningapp.ui.statistic.StatisticViewModel
import kotlinx.coroutines.launch

class StatusFragment : Fragment() {
    private lateinit var binding: FragmentStatusBinding
    private val viewModel by viewModels<UserViewModel> {
        val repository = UserRepository(RunningDatabase.getInstance(requireContext()).userDao())
        UserViewModel.Factory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnConfirm.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        setupObserve()
    }

    private fun setupObserve() {
        binding.tvSelectGender.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            if (selectedItem == "Male") {
                // TODO: do smt with male
            } else {
                // TODO: do smt with female
            }
        }
        setUpToggle()
    }


    private fun setUpToggle() {
        val btnFt = binding.btnFt
        val btnCm = binding.btnCm
        val btnKg = binding.btnKg
        val btnLbs = binding.btnLbs
        val hintHeight = binding.textHintHeightUnit
        val hintWeight = binding.textHintWeightUnit

        var height: Double = 0.0
        val age: Int = binding.edtAge.text.toString().toInt()
        var weight: Double = 0.0
        //ft
        btnFt.setOnClickListener {
            hintHeight.text = getString(R.string.text_ft)
            btnFt.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_gray))
            val heightFt: Double = binding.edtHeight.toString().toDouble()
            height = heightFt * 34.48
        }
        //cm
        btnCm.setOnClickListener {
            hintHeight.text = getString(R.string.text_cm)
            btnFt.setBackgroundColor(requireContext().getColor(R.color.main_gray))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            height = binding.edtHeight.toString().toDouble()
        }
        //kg
        btnKg.setOnClickListener {
            hintWeight.text = getString(R.string.text_kg)
            btnKg.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnLbs.setBackgroundColor(requireContext().getColor(R.color.main_gray))
            weight = binding.edtWeight.toString().toDouble()
        }
        //lbs
        btnLbs.setOnClickListener {
            hintWeight.text = getString(R.string.text_lbs)
            btnLbs.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnKg.setBackgroundColor(requireContext().getColor(R.color.main_gray))
            val weightLbs = binding.edtWeight.text.toString().toDouble()
            weight = weightLbs * 0.45
        }
        val user = User(age = age, height = height, weight = weight)
        viewModel.insertOneUser(user)
    }
}