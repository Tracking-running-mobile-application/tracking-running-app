package com.app.java.trackingrunningapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentEditProfileBinding
import com.app.java.trackingrunningapp.databinding.FragmentProfileBinding

class EditProfileFragment:Fragment() {
    private lateinit var binding: FragmentEditProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAffect()
        binding.btnSave.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupAffect() {
        val imageMale: ImageView = binding.imageMaleEdit
        val imageFemale: ImageView = binding.imageFemaleEdit
        val btnInch = binding.btnInchEdit
        val btnCm = binding.btnCmEdit
        val btnKg = binding.btnKgEdit
        val btnLbs = binding.btnLbsEdit

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