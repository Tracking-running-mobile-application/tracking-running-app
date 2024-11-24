package com.app.java.trackingrunningapp.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
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
        val bottomNav = binding.bottomNav
        bottomNav.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.profileFragment, R.id.statisticFragment)
        )

        // handle navigation event
        val icFilter = binding.toolbarMain.menu.findItem(R.id.toolbar_filter)
        icFilter.isVisible = false
        val icSettings = binding.toolbarMain.menu.findItem(R.id.toolbar_setting)
        icSettings.isVisible = false
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val tvTitle: TextView = binding.tvToolbarTitle
            binding.toolbarMain.setNavigationIcon(R.drawable.ic_arrow_back_24)
            binding.toolbarMain.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            when (destination.id) {
                R.id.homeFragment -> {
                    tvTitle.text = getString(R.string.text_home)
                    icSettings.isVisible = true
                    binding.toolbarMain.setNavigationIcon(R.drawable.ic_notification)
                    binding.toolbarMain.setNavigationOnClickListener {
                        // TODO: do something with notification
                        Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
                    }
                }

                R.id.profileFragment -> {
                    tvTitle.text = getString(R.string.text_profile)
                    binding.toolbarMain.setNavigationIcon(R.drawable.ic_edit)
                    binding.toolbarMain.setNavigationOnClickListener {
                       navController.navigate(R.id.action_profileFragment_to_editProfileFragment)
                    }
                }

                R.id.runFragment -> {
                    tvTitle.text = getString(R.string.text_run)
                }

                R.id.statisticFragment -> {
                    tvTitle.text = getString(R.string.text_statistics)
                }

                R.id.historyFragment -> {
                    tvTitle.text = getString(R.string.text_history)
                    icFilter.isVisible = true
                    icSettings.isVisible = false
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}