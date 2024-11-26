package com.app.java.trackingrunningapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView6.setOnClickListener{
            it.findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            // hide bottom nav
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE
        }
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.VISIBLE

        binding.btnSetting.setOnClickListener{
            it.findNavController().navigate(R.id.action_profileFragment_to_settingFragment)
        }
    }


}