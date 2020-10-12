package com.alex.newstime

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.alex.core.bus.RxBus
import com.alex.newstime.bus.AppEvent
import com.alex.newstime.feature.article.di.articleModule
import com.alex.newstime.feature.base.di.resourceProviderModule
import com.alex.newstime.feature.favorits.di.favoritsModule
import com.alex.newstime.feature.topheadlines.di.topHeadlinesModule
import com.alex.newstime.receiver.NetworkCallback
import com.alex.newstime.repository.api.ApiClient
import com.alex.newstime.repository.article.di.articleRepositoryModule
import com.alex.newstime.repository.database.NewstimeDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class NewsTimeApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        setup()
    }

    // ----------------------------------------------------------------------------

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppStart() {
        RxBus.publish(AppEvent(Lifecycle.Event.ON_START))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppStop() {
        RxBus.publish(AppEvent(Lifecycle.Event.ON_STOP))
    }

    // ----------------------------------------------------------------------------

    private fun setup() {
        setupDatabase()
        setupApi()
        setupConnectivityReceiver()
        setupTimber()
        setupKoin()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupDatabase() {
        NewstimeDatabase.init(this)
    }

    private fun setupApi() {
        ApiClient.initialize()
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
            modules(listOf(topHeadlinesModule, favoritsModule, articleModule, articleRepositoryModule, resourceProviderModule))
        }
    }
}