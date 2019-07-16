package com.alex.newstime

import android.app.Application
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.alex.newstime.bus.AppEvent
import com.alex.newstime.bus.RxBus
import com.alex.newstime.receiver.ConnectivityReceiver
import com.alex.newstime.repository.api.ApiClient
import com.alex.newstime.repository.database.NewstimeDatabase
import com.alex.newstime.repository.sharedpreference.RxSharedPreferences
import leakcanary.LeakCanary
import leakcanary.LeakSentry
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
        setupLeakCanary()
        setupSharedPreferences()
        setupDatabase()
        setupApi()
        setupConnectivityReceiver()
        setupTimber()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupSharedPreferences() {
        RxSharedPreferences.initialize(this)
    }

    private fun setupDatabase() {
        NewstimeDatabase.init(this)
    }

    private fun setupApi() {
        ApiClient.initialize()
    }

    private fun setupLeakCanary() {
        if (!BuildConfig.DEBUG) return

        LeakSentry.config = LeakSentry.config.copy(watchFragmentViews = false)
        LeakCanary.config = LeakCanary.config.copy(dumpHeap = true)
    }

    private fun setupConnectivityReceiver() {
        registerReceiver(ConnectivityReceiver(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }

    private fun setupTimber() {
        if (!BuildConfig.DEBUG) return

        Timber.plant(Timber.DebugTree())
    }
}