package com.alex.newstime.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.alex.newstime.bus.ConnectivityEvent
import com.alex.newstime.bus.RxBus

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val res = networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected

        RxBus.publish(ConnectivityEvent(res))
    }
}