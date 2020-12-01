package com.alex.newstime

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.alex.newstime.feature.article.di.articleModule
import com.alex.newstime.feature.base.di.resourceProviderModule
import com.alex.newstime.feature.topheadlines.di.topHeadlinesModule
import com.alex.newstime.receiver.NetworkCallback
import com.alex.newstime.repository.article.di.articleRepositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class NewsTimeApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        setup()
    }

    // ----------------------------------------------------------------------------

    private fun setup() {
        setupConnectivityReceiver()
        setupTimber()
        setupKoin()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupConnectivityReceiver() {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        manager.registerNetworkCallback(NetworkRequest.Builder().build(), NetworkCallback())
    }

    private fun setupTimber() {
        if (!BuildConfig.DEBUG) return

        Timber.plant(Timber.DebugTree())
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@NewsTimeApplication)
            modules(listOf(
                // features
                topHeadlinesModule,
                articleModule,
                // repositories
                articleRepositoryModule,
                // resources
                resourceProviderModule))
        }
    }
}