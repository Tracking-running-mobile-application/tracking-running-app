package com.app.java.trackingrunningapp.ui.personalGoal

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.java.trackingrunningapp.databinding.FragmentPersonalGoalBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PersonalGoalFragment : Fragment() {

    private lateinit var dayAdapter: DayAdapter
    private val dayList = mutableListOf(
        DayItem("Every Monday", false),
        DayItem("Every Tuesday", false),
        DayItem("Every Wednesday", false),
        DayItem("Every Thursday", false),
        DayItem("Every Friday", false),
        DayItem("Every Saturday", false),
        DayItem("Every Sunday", false)
    )

    private var _binding: FragmentPersonalGoalBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPersonalGoalBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //define section
        val button1 = binding.objective1
        val button2 = binding.objective2
        val button3 = binding.objective3
        val objeciveBar = binding.objectiveBox
        val unitText = binding.unitText

        //button
        button1.setOnClickListener { chooseObjective(objeciveBar, unitText, button1, button2, button3)}
        button2.setOnClickListener { chooseObjective(objeciveBar, unitText, button2, button1, button3)}
        button3.setOnClickListener { chooseObjective(objeciveBar, unitText, button3, button1, button2)}

        //frequency recycle view
        val recyclerView = binding.recycleView
        recyclerView.layoutManager = LinearLayoutManager(context)
        dayAdapter = DayAdapter(dayList)
        recyclerView.adapter = dayAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun chooseObjective(objective:EditText, unit:TextView, selected:Button, other1:Button, other2:Button) {

        val selectedBackground = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 20f
            setColor(Color.parseColor("#EEFF01"))
        }

        val NonSelectedBackground = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 20f
            setColor(Color.parseColor("#3E3E3E"))
        }

        //selected
        selected.background = selectedBackground
        selected.setTextColor(Color.parseColor("#3E3E3E"))

        //set hint and text in Objective box
        objective.hint = selected.text
        unit.text = selected.tag.toString()

        //others
        other1.background = NonSelectedBackground
        other1.setTextColor(Color.parseColor("#ffffff"))

        other2.background = NonSelectedBackground
        other2.setTextColor(Color.parseColor("#ffffff"))
    }
}