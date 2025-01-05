package com.app.java.trackingrunningapp.ui.home.personalGoal

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.databinding.FragmentPersonalGoalBinding
import com.app.java.trackingrunningapp.ui.viewmodel.PersonalGoalViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.PersonalGoalViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapbox.maps.extension.style.expressions.dsl.generated.distance

class PersonalGoalFragment : Fragment() {
    private lateinit var binding: FragmentPersonalGoalBinding
    private lateinit var personalGoalViewModel: PersonalGoalViewModel
    private var isDistanceClicked: Boolean = false
    private var isDurationClicked: Boolean = false
    private var isCaloClicked: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // init personal goal viewmodel
        val personalGoalViewModelFactory = PersonalGoalViewModelFactory(
            InitDatabase.personalGoalRepository,
            InitDatabase.runSessionRepository
        )
        personalGoalViewModel = ViewModelProvider(
            this,
            personalGoalViewModelFactory
        )[PersonalGoalViewModel::class.java]

        binding = FragmentPersonalGoalBinding.inflate(inflater, container, false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).isVisible = false
        requireActivity().findViewById<TextView>(R.id.tv_toolbar_title).text = "Personal Goal"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        handleClickEvent()
    }

    private fun handleClickEvent() {
        binding.btnGoalDiscard.setOnClickListener {
            // back to home
            it.findNavController().navigate(R.id.action_global_homeFragment)
        }
        binding.btnGoalSave.setOnClickListener {
            // TODO: Save plan
            if (isDistanceClicked) {
                personalGoalViewModel.upsertPersonalGoal(
                    name = binding.editCustomNamePlan.text.toString(),
                    targetDistance = binding.objectiveBox.text.toString().toDouble(),
                    targetDuration = 0.0,
                    targetCaloriesBurned = 0.0
                )
            } else if (isDurationClicked) {
                personalGoalViewModel.upsertPersonalGoal(
                    name = binding.editCustomNamePlan.text.toString(),
                    targetDistance = 0.0,
                    targetDuration = binding.objectiveBox.text.toString().toDouble(),
                    targetCaloriesBurned = 0.0
                )
            } else if (isCaloClicked) {
                personalGoalViewModel.upsertPersonalGoal(
                    name = binding.editCustomNamePlan.text.toString(),
                    targetDistance = 0.0,
                    targetDuration = 0.0,
                    targetCaloriesBurned = binding.objectiveBox.text.toString().toDouble()
                )
            }
            // back to home
            it.findNavController().navigate(R.id.action_global_homeFragment)
        }
    }

    private fun setupView() {
        //define section
        val buttonDistance = binding.btnObjectDistance
        val buttonDuration = binding.btnObjectDuration
        val buttonCalo = binding.btnObjectCalo
        val objectiveBar = binding.objectiveBox
        val unitText = binding.unitText
        //button
        buttonDistance.setOnClickListener {
            isDistanceClicked = true
            isDurationClicked = false
            isCaloClicked = false
            chooseObjective(
                objectiveBar,
                unitText,
                buttonDistance,
                buttonDuration,
                buttonCalo
            )
        }
//        buttonDistance.performClick()
        buttonDuration.setOnClickListener {
            isDistanceClicked = false
            isDurationClicked = true
            isCaloClicked = false
            chooseObjective(
                objectiveBar,
                unitText,
                buttonDuration,
                buttonDistance,
                buttonCalo
            )
        }
        buttonCalo.setOnClickListener {
            isDistanceClicked = false
            isDurationClicked = false
            isCaloClicked = true
            chooseObjective(
                objectiveBar,
                unitText,
                buttonCalo,
                buttonDistance,
                buttonDuration
            )
        }
    }

    private fun chooseObjective(
        objective: EditText,
        unit: TextView,
        selected: Button,
        nonSelected1: Button,
        nonSelected2: Button
    ) {
        val selectedBackground = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 16f
            setColor(Color.parseColor("#EEFF01"))
        }
        val nonSelectedBackground = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 16f
            setColor(Color.parseColor("#3E3E3E"))
            setStroke(4, Color.parseColor("#000000"))
        }
        //selected
        selected.background = selectedBackground
        selected.setTextColor(Color.parseColor("#3E3E3E"))
        //set hint and text in Objective box
        objective.hint = selected.text
        unit.text = selected.tag.toString()
        //others
        nonSelected1.background = nonSelectedBackground
        nonSelected1.setTextColor(Color.parseColor("#ffffff"))
        nonSelected2.background = nonSelectedBackground
        nonSelected2.setTextColor(Color.parseColor("#ffffff"))
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).isVisible = true
    }
}