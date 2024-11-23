package com.app.java.trackingrunningapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.room.Room
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.modelbase.RunningDatabase

class IntroActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    companion object {
        lateinit var runningDatabase: RunningDatabase
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initNavHost()
        runningDatabase = Room.databaseBuilder(
            applicationContext,
            RunningDatabase::class.java,
            "running_database"
        ).build()
    }

    private fun initNavHost() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_intro_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}