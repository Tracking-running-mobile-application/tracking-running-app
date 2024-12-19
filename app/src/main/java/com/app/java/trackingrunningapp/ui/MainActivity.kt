package com.app.java.trackingrunningapp.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavHost()
    }

    private fun initNavHost() {
        // setup nav host
        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        // setup bottom nav
        val bottomNav = binding.bottomNav
        bottomNav.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.profileFragment, R.id.statisticFragment,
                R.id.profileFragment,R.id.historyFragment)
        )
        handleNavigation()
    }

    private fun handleNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val tvTitle: TextView = binding.tvToolbarTitle
            binding.toolbarMain.setNavigationIcon(R.drawable.ic_arrow_back_24)
            binding.toolbarMain.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            val icSettings = binding.toolbarMain.menu.findItem(R.id.item_toolbar_setting)
            val icFilter = binding.toolbarMain.menu.findItem(R.id.item_toolbar_filter)
            val icEdit = binding.toolbarMain.menu.findItem(R.id.item_toolbar_edit)
            when (destination.id) {
                R.id.homeFragment -> {
                    tvTitle.text = getString(R.string.text_home)
                    icSettings.isVisible = true
                    binding.toolbarMain.setNavigationIcon(R.drawable.ic_notification)
                    binding.toolbarMain.setNavigationOnClickListener {
                        // TODO: do something with notification
                        Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
                    }
                    // navigate to setting
                    icSettings.setOnMenuItemClickListener{
                        navController.navigate(R.id.action_homeFragment_to_settingFragment2)
                        true
                    }
                }
                R.id.profileFragment -> {
                    tvTitle.text = getString(R.string.text_profile)
                    icEdit.isVisible = true
                    icEdit.setOnMenuItemClickListener {
                        navController.navigate(R.id.action_profileFragment_to_editProfileFragment)
                        true
                    }
                }
                R.id.runFragment -> {
                    icSettings.isVisible = false
                    tvTitle.text = getString(R.string.text_run)
                }
                R.id.statisticFragment -> {
                    icSettings.isVisible = false
                    tvTitle.text = getString(R.string.text_statistics)
                }
                R.id.historyFragment -> {
                    tvTitle.text = getString(R.string.text_history)
                    icFilter.isVisible = true
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}