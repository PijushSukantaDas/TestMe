package com.silverblaze.myapplication.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.silverblaze.myapplication.R
import com.silverblaze.myapplication.databinding.ActivityAppBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAppBinding>(
            this,
            R.layout.activity_app
        )

        binding.userBtn.setOnClickListener {
            findNavController(R.id.nav_host_app_fragment).navigate(R.id.usersFragment)
        }

        binding.profileBtn.setOnClickListener {
            findNavController(R.id.nav_host_app_fragment).navigate(R.id.profileFragment)
        }

        binding.exitBtn.setOnClickListener {
            finishAffinity()
        }


    }
}