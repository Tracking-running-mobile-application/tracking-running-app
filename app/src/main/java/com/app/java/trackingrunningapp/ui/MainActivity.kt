package com.app.java.trackingrunningapp.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.databinding.ActivityMainBinding
import com.app.java.trackingrunningapp.model.repositories.NotificationRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var notificationRepository: NotificationRepository = InitDatabase.notificationRepository
    private var errorNoti: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val language = LocaleUtils.getLanguagePreference(this)
        LocaleUtils.setLocale(this, language)
        errorNoti = false

        setContentView(binding.root)
        initNavHost()
        setUpNetwork()
    }

    private fun setUpNetwork() {
        lifecycleScope.launch {
            if (!isOnline() && !errorNoti) {
                notificationRepository.triggerNotification(type = "NO_NETWORK")
                errorNoti = true
            }
        }
    }

    private fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
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
            setOf(
                R.id.homeFragment, R.id.profileFragment, R.id.statisticFragment,
                R.id.profileFragment, R.id.historyFragment
            )
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
                    icSettings.setOnMenuItemClickListener {
                        navController.navigate(R.id.action_homeFragment_to_settingFragment2)
                        true
                    }
                    binding.bottomNav.isVisible = true
                }

                R.id.profileFragment -> {
                    tvTitle.text = getString(R.string.text_profile)
                    icEdit.isVisible = true
                    icEdit.setOnMenuItemClickListener {
                        navController.navigate(R.id.action_profileFragment_to_editProfileFragment)
                        true
                    }
                    binding.bottomNav.isVisible = true
                }

                R.id.runFragment -> {
                    icSettings.isVisible = false
                    tvTitle.text = getString(R.string.text_run)
                    binding.bottomNav.isVisible = true
                }

                R.id.statisticFragment -> {
                    icSettings.isVisible = false
                    tvTitle.text = getString(R.string.text_statistics)
                    binding.bottomNav.isVisible = true
                }

                R.id.historyFragment -> {
                    tvTitle.text = getString(R.string.text_history)
                    icFilter.isVisible = true
                    binding.bottomNav.isVisible = true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}