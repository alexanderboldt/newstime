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
import com.alex.newstime.repository.sharedpreference.RxSharedPreferences
import com.squareup.leakcanary.LeakCanary

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

        // check if leakCanary could be initialized
        if (!setupLeakCanary()) {
            return
        }

        setupSharedPreferences()
        setupApi()
        setupConnectivityReceiver()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupSharedPreferences() {
        RxSharedPreferences.initialize(this)
    }

    private fun setupApi() {
        return ApiClient.initialize()
    }

    private fun setupLeakCanary(): Boolean {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return false
        }

        LeakCanary.install(this)

        return true
    }

    private fun setupConnectivityReceiver() {
        registerReceiver(ConnectivityReceiver(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }
}