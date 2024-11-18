package com.app.java.trackingrunningapp.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.app.java.trackingrunningapp.R

@Suppress("UNREACHABLE_CODE")
class SettingFragment : Fragment() {
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        spinner1 = view.findViewById(R.id.theme)
        var themeItems = listOf("Light Theme", "Dark Theme", "Custom Theme", "High Contrast Theme", "Material Design Theme", "Flat Design Theme", "Syntax Highlighting Themes", "IDE Themes", "Debugger Themes", "Feature-Based Themes", "Module-Specific Themes", "Mobile vs. Desktop Themes", "E-commerce Theme", "Gaming Theme", "Health and Fitness Theme", "Educational Theme")
        var themeAdapter = ArrayAdapter(requireContext(),R.layout.item_setting_theme,themeItems)
        themeAdapter.setDropDownViewResource(R.layout.item_setting_theme)
        spinner1.adapter = themeAdapter
        spinner1.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })

        spinner2 = view.findViewById(R.id.unit)
        val unitItems = listOf("Millimeter (mm)", "Centimeter (cm)", "Meter (m)", "Kilometer (km)", "Inch (in)", "Foot (ft)", "Yard (yd)", "Mile (mi)", "Nautical Mile (nmi)")
        var unitAdapter = ArrayAdapter(requireContext(),R.layout.item_setting_unit,unitItems)
        unitAdapter.setDropDownViewResource(R.layout.item_setting_unit)
        spinner2.adapter = unitAdapter
        spinner2.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })

        spinner3 = view.findViewById(R.id.language)
        var languageItems = listOf("English", "German", "French", "Spanish", "Italian", "Japanese", "Chinese", "Korean", "Portuguese", "Russian")
        var languageAdapter = ArrayAdapter(requireContext(),R.layout.item_setting_language,languageItems)
        languageAdapter.setDropDownViewResource(R.layout.item_setting_language)
        spinner3.adapter = languageAdapter
        spinner3.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                Toast.makeText(requireContext(), "Selected: $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
    return view
    }
}