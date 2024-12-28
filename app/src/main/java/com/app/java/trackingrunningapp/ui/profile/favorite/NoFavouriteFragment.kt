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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

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
        val toolbarTitle = requireActivity().findViewById<TextView>(R.id.tv_toolbar_title)
        toolbarTitle.text =
            getString(R.string.text_favourite_run)
        // hide bottom nav
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE
        // notify
        Snackbar.make(
            binding.containerLayoutNoFavourite,
            "Please Come To History Section To Add Your New Favourite Run.",
            Snackbar.LENGTH_SHORT
        ).show()
        // hide setting
        requireActivity().findViewById<Toolbar>(R.id.toolbar_main)
            .menu.findItem(R.id.item_toolbar_setting)
            .isVisible = false
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility =
            View.VISIBLE
    }
}