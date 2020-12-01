package com.alex.newstime.receiver

import android.net.ConnectivityManager
import android.net.Network
import com.alex.newstime.bus.ConnectivityEvent
import org.greenrobot.eventbus.EventBus

class NetworkCallback : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)

        EventBus.getDefault().post(ConnectivityEvent(true))
    }

    override fun onLost(network: Network) {
        super.onLost(network)

        EventBus.getDefault().post(ConnectivityEvent(false))
    }
}