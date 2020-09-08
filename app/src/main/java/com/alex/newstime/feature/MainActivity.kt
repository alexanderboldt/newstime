package com.alex.newstime.feature

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import com.alex.newstime.BuildConfig
import com.alex.newstime.R
import com.alex.newstime.databinding.ActivityMainBinding
import com.alex.newstime.feature.favorits.FavoritsFragment
import com.alex.newstime.feature.topheadlines.TopHeadlinesFragment

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // ----------------------------------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.textViewVersion.apply {
            text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            updateLayoutParams<ConstraintLayout.LayoutParams> { topMargin = getStatusBarHeight() }
        }

        binding.bottomNavigation.also {
            it.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryColor))
            it.setOnNavigationItemSelectedListener { menuItem ->
                val fragment = when (menuItem.itemId) {
                    R.id.item_one -> TopHeadlinesFragment()
                    else -> FavoritsFragment()
                }
                supportFragmentManager.commit {
                    replace(R.id.frameLayout_fragments, fragment)
                }
                true
            }
        }

        supportFragmentManager.commit {
            add(R.id.frameLayout_fragments, TopHeadlinesFragment())
        }
    }

    // ----------------------------------------------------------------------------

    private fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return when (resourceId > 0) {
            true -> resources.getDimensionPixelSize(resourceId)
            false -> 0
        }
    }
}