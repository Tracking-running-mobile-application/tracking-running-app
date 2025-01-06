package com.app.java.trackingrunningapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.java.trackingrunningapp.R
import com.app.java.trackingrunningapp.data.database.InitDatabase
import com.app.java.trackingrunningapp.data.repository.UserRepository
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModel
import com.app.java.trackingrunningapp.ui.viewmodel.UserViewModelFactory


class IntroActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var userViewModel: UserViewModel
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
//        val userRepository = UserRepository()
////        val userFactory = UserViewModelFactory(InitDatabase.userRepository)
//        val userFactory = UserViewModelFactory(userRepository)
//        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]
//        userViewModel.fetchUserInfo()
//        userViewModel.userLiveData.observe(this){user->
//            Log.d("userIntro","$user")
//            if(user != null){
//                startActivity(Intent(this,MainActivity::class.java))
//                Log.d("user","$user")
//            }
//        }
    }

    private fun initNavHost() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_intro_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}