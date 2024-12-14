package com.app.java.trackingrunningapp.ui.profile.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.FragmentNoFavouriteBinding

class NoFavouriteFragment : Fragment() {
    private lateinit var binding: FragmentNoFavouriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // change toolbar title
        val toolbarTitle =  requireActivity().findViewById<TextView>(R.id.tv_toolbar_title)
        toolbarTitle.text =
            getString(R.string.text_favourite_run)
        // navigate to history
        binding.textAddFavourite.setOnClickListener {
            it.findNavController().navigate(R.id.action_noFavouriteFragment_to_historyFragment2)
            toolbarTitle.text = getString(R.string.text_history)
        }
        // hide setting
        requireActivity().findViewById<Toolbar>(R.id.toolbar_main)
            .menu.findItem(R.id.item_toolbar_setting)
            .isVisible = false
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<Toolbar>(R.id.toolbar_main)
            .menu.findItem(R.id.item_toolbar_setting)
            .isVisible = true
    }
}