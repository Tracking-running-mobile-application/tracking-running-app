package com.app.java.trackingrunningapp.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentStatusBinding

class StatusFragment : Fragment() {
    private lateinit var binding: FragmentStatusBinding

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
        val imageMale: ImageView = binding.imageMale
        val imageFemale: ImageView = binding.imageFemale
        val btnInch = binding.btnInch
        val btnCm = binding.btnCm
        val btnKg = binding.btnKg
        val btnLbs = binding.btnLbs

        // male
        imageMale.setOnClickListener {
            binding.textGender.text = getString(R.string.text_gender, "Male")
            imageMale.setImageResource(R.drawable.img_select_male)
            imageFemale.setImageResource(R.drawable.img_female)
        }
        // female
        imageFemale.setOnClickListener {
            binding.textGender.text = getString(R.string.text_gender, "Female")
            imageFemale.setImageResource(R.drawable.img_select_female)
            imageMale.setImageResource(R.drawable.img_male)
        }

        //inch
        btnInch.setOnClickListener {
            btnInch.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
        //cm
        btnCm.setOnClickListener {
            btnInch.setBackgroundColor(requireContext().getColor(R.color.main_gray))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
        }
        btnKg.setOnClickListener {
            btnKg.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnLbs.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
        btnLbs.setOnClickListener {
            btnLbs.setBackgroundColor(requireContext().getColor(R.color.main_yellow))
            btnKg.setBackgroundColor(requireContext().getColor(R.color.main_gray))
        }
    }
}