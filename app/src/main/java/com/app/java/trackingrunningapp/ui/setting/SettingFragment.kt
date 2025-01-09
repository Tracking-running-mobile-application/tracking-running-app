package com.app.java.trackingrunningapp.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentSettingBinding
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        val userRepository = UserRepository()
        val userFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // hide icon setting
        val itemSetting = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main).menu.findItem(R.id.item_toolbar_setting)
        itemSetting.isVisible = false
        setupUnit()
        setupLanguages()
        binding.btnConfirmSetting.setOnClickListener {
            recreate(requireActivity()) // Reload activity
            it.findNavController().popBackStack(R.id.homeFragment,false)
        }
    }

    private fun setupUnit() {
        binding.tvSelectUnit.setOnItemClickListener { _, _, position, _ ->
            when(position){
                0->{ // km
                    lifecycleScope.launch {
                        userViewModel.fetchUserInfo()
                        userViewModel.userLiveData.observe(viewLifecycleOwner){
                            userViewModel.upsertUserInfo(
                                name = it?.name,
                                age = it?.age,
                                height = it?.height,
                                weight = it?.weight!!,
                                metricPreference = User.KILOGRAM,
                                unit = User.UNIT_KM
                            )
                        }
                    }
                }
                1->{ // miles
                    lifecycleScope.launch {
                        userViewModel.fetchUserInfo()
                        userViewModel.userLiveData.observe(viewLifecycleOwner){
                            userViewModel.upsertUserInfo(
                                name = it?.name,
                                age = it?.age,
                                height = it?.height,
                                weight = it?.weight!!,
                                metricPreference = User.KILOGRAM,
                                unit = User.UNIT_MILE
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setupLanguages() {
        binding.tvSelectLanguage.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> { // English
                    LocaleUtils.setLocale(requireContext(), "en")
//                    recreate(requireActivity()) // Reload activity
                }
                1 -> { // Vietnamese
                    LocaleUtils.setLocale(requireContext(), "vi")
//                    recreate(requireActivity()) // Reload activity
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        val itemSetting = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main).menu.findItem(R.id.item_toolbar_setting)
        itemSetting.isVisible = true
    }
}