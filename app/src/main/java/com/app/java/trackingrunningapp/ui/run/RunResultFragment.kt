package com.app.java.trackingrunningapp.ui.run

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentRunResultBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class RunResultFragment : Fragment() {
    private lateinit var binding:FragmentRunResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRunResultBinding.inflate(inflater,container,false)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpAction()
    }

    private fun setUpAction() {
        binding.btnRunSave.setOnClickListener {
            it.findNavController().navigate(R.id.action_global_runFragment)
            Toast.makeText(requireContext(),"Added Successful",Toast.LENGTH_SHORT).show()
        }
        binding.btnDiscard.setOnClickListener {
            it.findNavController().navigate(R.id.action_global_runFragment)
            Toast.makeText(requireContext(),"Added Failure",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpToolbar() {
        val toolbarTitle = requireActivity().findViewById<TextView>(R.id.tv_toolbar_title)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_main)
        toolbar.navigationIcon = null
        toolbarTitle.text = "Run Result"
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.VISIBLE
    }
}