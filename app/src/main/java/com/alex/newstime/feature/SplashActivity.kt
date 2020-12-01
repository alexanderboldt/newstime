package com.alex.newstime.feature

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // explore: what if we want to show custom stuff?

        startActivity(Intent(this, MainActivity::class.java))

        finish()
    }
}