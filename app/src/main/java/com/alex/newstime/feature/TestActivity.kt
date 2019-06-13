package com.alex.newstime.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router

class TestActivity : AppCompatActivity() {

    lateinit var router: Router

    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = ChangeHandlerFrameLayout(this)

        setContentView(layout)

        router = Conductor.attachRouter(this, layout, savedInstanceState)
    }
}