package com.alex.newstime.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.alex.newstime.R
import com.alex.newstime.databinding.ActivityMainBinding
import com.alex.newstime.feature.favorits.FavoritsController
import com.alex.newstime.feature.topheadlines.TopHeadlinesController
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var router: Router

    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.bottomNavigation.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryColor))
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val controller = when (it.itemId) {
                R.id.item_one -> TopHeadlinesController()
                else -> FavoritsController()
            }
            router.apply {
                popToRoot()
                setRoot(RouterTransaction.with(controller))
            }
            true
        }

        router = Conductor.attachRouter(this, binding.changeHandlerFrameLayout, savedInstanceState)

        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(TopHeadlinesController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}