package com.example.mynews.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.example.mynews.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        lifecycleScope.launch {
            delay(5000)
            val intent = Intent(this@SplashActivity,NewsActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onStop() {
        finish()
        super.onStop()
    }
}