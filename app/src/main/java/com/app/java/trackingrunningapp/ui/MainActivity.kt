package com.app.java.trackingrunningapp.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView

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
        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNav = binding.bottomNav
        bottomNav.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.profileFragment, R.id.statisticFragment)
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val tvTitle: TextView = binding.tvToolbarTitle
            if(destination.id == R.id.homeFragment){
                tvTitle.text = getString(R.string.text_home)
//                binding.imgIcSettings.visibility = View.VISIBLE
                binding.toolbarMain.setNavigationIcon(R.drawable.ic_notification)
            }else{
//                binding.imgIcSettings.visibility = View.GONE
//                binding.imgIcFilter?.visibility = View.VISIBLE
                binding.toolbarMain.setNavigationIcon(R.drawable.ic_arrow_back_24)
                binding.toolbarMain.setNavigationOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
                when (destination.id) {
                    R.id.profileFragment ->{
                        tvTitle.text = getString(R.string.text_profile)
                    }
                    R.id.runFragment ->{
                        tvTitle.text = getString(R.string.text_run)
                    }
                    R.id.statisticFragment -> {
                        tvTitle.text = getString(R.string.text_statistics)
                    }
                    R.id.historyFragment ->{
                        tvTitle.text = getString(R.string.text_history)
                    }
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

}