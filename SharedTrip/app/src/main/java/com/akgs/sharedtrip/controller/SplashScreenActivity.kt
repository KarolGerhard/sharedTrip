package com.akgs.sharedtrip.controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.akgs.sharedtrip.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.statusBarColor = Color.parseColor("#297BF5")


        Handler(Looper.getMainLooper()).postDelayed({
           val intent = Intent(this, LoginActivity::class.java)
           startActivity(intent)
           finish()
        }, 3000)
    }
}