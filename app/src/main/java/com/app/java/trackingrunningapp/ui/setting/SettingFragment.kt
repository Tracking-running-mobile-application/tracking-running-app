package com.app.java.trackingrunningapp.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.model.entity.User
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.databinding.FragmentSettingBinding
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var settingViewModel: SettingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        val userFactory = UserViewModelFactory(InitDatabase.userRepository)
        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]
        userViewModel.fetchUserInfo()
        userViewModel.userLiveData.observe(viewLifecycleOwner) {user->
            if(user?.unit == User.UNIT_KM){
                binding.tvSelectUnit.hint = getString(R.string.km)
            }else if(user?.unit == User.UNIT_MILE){
                binding.tvSelectUnit.hint = getString(R.string.mile)
            }
        }
        settingViewModel = ViewModelProvider(requireActivity())[SettingViewModel::class.java]
        settingViewModel.selectedLanguage.observe(viewLifecycleOwner){
            binding.tvSelectLanguage.hint = it
        }
        requireActivity().findViewById<TextView>(R.id.tv_toolbar_title).text =
            getString(R.string.text_settings)
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
            it.findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    private fun setupUnit() {
        binding.tvSelectUnit.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> { // km
                    lifecycleScope.launch {
                        userViewModel.fetchUserInfo()
                        userViewModel.userLiveData.observe(viewLifecycleOwner) {
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

                1 -> { // miles
                    lifecycleScope.launch {
                        userViewModel.fetchUserInfo()
                        userViewModel.userLiveData.observe(viewLifecycleOwner) {
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
                    settingViewModel.updateLanguage(getString(R.string.text_english))
                }

                1 -> { // Vietnamese
                    LocaleUtils.setLocale(requireContext(), "vi")
//                    recreate(requireActivity()) // Reload activity
                    settingViewModel.updateLanguage(getString(R.string.text_vietnamese))
                }
            }
            reloadBottomNavMenu()
            recreate(requireActivity()) // Reload Activity
        }
    }
    private fun reloadBottomNavMenu() {
        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.menu.clear()
        bottomNav.inflateMenu(R.menu.bottom_nav) // Load lại menu
    }


    override fun onStop() {
        super.onStop()
        val itemSetting = requireActivity()
            .findViewById<Toolbar>(R.id.toolbar_main).menu.findItem(R.id.item_toolbar_setting)
        itemSetting.isVisible = true
    }

    companion object {
        const val EXTRA_UNIT = "EXTRA_UNIT"
    }
}