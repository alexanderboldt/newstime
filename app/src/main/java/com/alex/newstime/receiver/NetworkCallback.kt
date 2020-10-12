package com.alex.newstime.receiver

import android.net.ConnectivityManager
import android.net.Network
import com.alex.core.bus.RxBus
import com.alex.newstime.bus.ConnectivityEvent

class NetworkCallback : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        super.onAvailable(network)

        RxBus.publish(ConnectivityEvent(true))
    }

    override fun onLost(network: Network) {
        super.onLost(network)

        RxBus.publish(ConnectivityEvent(false))
    }
}