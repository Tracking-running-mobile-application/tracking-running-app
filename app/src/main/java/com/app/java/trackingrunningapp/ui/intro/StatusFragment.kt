package com.app.java.trackingrunningapp.ui.intro

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.core.content.ContextCompat
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

        binding.imageMale.setOnClickListener {
            binding.textGender.text = getString(R.string.gender, "Male")
            imageMale.setImageResource(R.drawable.img_select_male)
            imageFemale.setImageResource(R.drawable.img_female)
        }
        binding.imageFemale.setOnClickListener {
            binding.textGender.text = getString(R.string.gender, "Female")
            imageFemale.setImageResource(R.drawable.img_select_female)
            imageMale.setImageResource(R.drawable.img_male)
        }

        binding.btnInch.setOnClickListener {
            btnInch.setBackgroundColor(requireContext().getColor(R.color.black))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.purple))
        }
        binding.btnCm.setOnClickListener {
            btnInch.setBackgroundColor(requireContext().getColor(R.color.purple))
            btnCm.setBackgroundColor(requireContext().getColor(R.color.black))
        }
    }
}