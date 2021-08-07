package com.silverblaze.myapplication.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.silverblaze.myapplication.R

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }
}