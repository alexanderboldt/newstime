package com.alex.newstime.feature

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import com.alex.newstime.BuildConfig
import com.alex.newstime.R
import com.alex.newstime.databinding.ActivityMainBinding
import com.alex.newstime.feature.favorits.FavoritsController
import com.alex.newstime.feature.topheadlines.TopHeadlinesController
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import tech.linjiang.pandora.Pandora

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var router: Router

    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.textViewVersion.apply {
            text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            updateLayoutParams<ConstraintLayout.LayoutParams> { topMargin = getStatusBarHeight() }
            setOnLongClickListener {
                Pandora.get().open()
                false
            }
        }

        binding.bottomNavigation.also {
            it.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryColor))
            it.setOnNavigationItemSelectedListener { menuItem ->
                val controller = when (menuItem.itemId) {
                    R.id.item_one -> TopHeadlinesController()
                    else -> FavoritsController()
                }
                router.apply {
                    popToRoot()
                    setRoot(RouterTransaction.with(controller as Controller))
                }
                true
            }
        }

        router = Conductor.attachRouter(this, binding.changeHandlerFrameLayout, savedInstanceState)

        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(TopHeadlinesController()))
        }

        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }

    // ----------------------------------------------------------------------------

    fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return when (resourceId > 0) {
            true -> resources.getDimensionPixelSize(resourceId)
            false -> 0
        }
    }
}