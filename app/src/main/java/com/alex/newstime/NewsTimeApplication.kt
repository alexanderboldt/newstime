package com.alex.newstime

import android.app.Application
import android.content.IntentFilter
import com.alex.newstime.receiver.ConnectivityReceiver
import com.alex.newstime.repository.api.ApiClient
import com.alex.newstime.repository.sharedpreference.RxSharedPreferences
import com.squareup.leakcanary.LeakCanary

class NewsTimeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setup()
    }

    // ----------------------------------------------------------------------------

    private fun setup() {

        // check if leakCanary could be initialized
        if (!setupLeakCanary()) {
            return
        }

        setupSharedPreferences()
        setupApi()
        setupConnectivityRecevier()
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

    private fun setupConnectivityRecevier() {
        registerReceiver(ConnectivityReceiver(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))
    }
}