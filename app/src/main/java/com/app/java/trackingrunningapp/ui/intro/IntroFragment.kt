package com.app.java.trackingrunningapp.ui.intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentIntroBinding
import com.app.java.trackingrunningapp.ui.MainActivity
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory

class IntroFragment : Fragment() {
    private lateinit var binding: FragmentIntroBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        val userRepository = UserRepository()
        val userFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]
        userViewModel.fetchUserInfo()
        userViewModel.userLiveData.observe(viewLifecycleOwner){user->
            Log.d("userIntro","$user")
            if(user != null){
                findNavController().navigate(R.id.action_introFragment_to_mainActivity)
                Log.d("user","$user")
                binding.btnGetStarted.visibility = View.INVISIBLE
            }
        }
        binding.btnGetStarted.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_statusFragment)
        }
    }
}