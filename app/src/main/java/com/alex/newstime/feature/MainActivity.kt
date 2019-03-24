package com.alex.newstime.feature

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alex.newstime.R
import com.alex.newstime.databinding.ActivityMainBinding
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

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val controller = when (it.itemId) {
                R.id.item_one -> TopHeadlinesController()
                else -> object: AbstractController() {
                    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
                        val view = View(context)
                        view.setBackgroundColor(0xff996611.toInt())

                        return view
                    }
                }
            }

            router.setRoot(RouterTransaction.with(controller))

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