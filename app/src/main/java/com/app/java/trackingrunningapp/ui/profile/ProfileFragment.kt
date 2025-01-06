package com.app.java.trackingrunningapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentProfileBinding
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userFactory = UserViewModelFactory(UserRepository())
        userViewModel = ViewModelProvider(requireActivity(),userFactory)[UserViewModel::class.java]
        userViewModel.fetchUserInfo()
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.userLiveData.observe(viewLifecycleOwner){
            binding.textProfileName.text = it?.name
            binding.textProfileAge.text = it?.age.toString()
            binding.textProfileWeight.text = it?.weight.toString()
            binding.textProfileHeight.text = it?.height.toString()
        }
        setupBarChart()
        navigateToFavourite()
    }

    private fun navigateToFavourite() {
        binding.cvFavoriteRun.setOnClickListener{
            it.findNavController().navigate(R.id.action_profileFragment_to_noFavouriteFragment)
        }
    }

    private fun setupBarChart() {
        val barSet = listOf(
            "Mon" to 4F,
            "Tue" to 2F,
            "Wed" to 4.5F,
            "Thu" to 3F,
            "Fri" to 6F,
            "Sat" to 1.5F,
            "Sun" to 4F
        )
        val barChart = binding.barchart
        barChart.animate(barSet)
        barChart.apply {
            animation.duration = 1000L
            labelsFormatter = { it.toInt().toString() }
        }
    }

    override fun onStop() {
        super.onStop()
        val toolbar = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main)
        val icEdit = toolbar.menu.findItem(R.id.item_toolbar_edit)
        icEdit.isVisible = false
    }
}