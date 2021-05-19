package com.decagon.android.sq007.SecondImplementation

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class LiveData(private val connectivityManager:ConnectivityManager):LiveData<Boolean>() {
    override fun onActive(){
        super.onActive()
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
    }
    override fun onInactive(){
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
    constructor(application: Application): this(application.getSystemService(
            Context.CONNECTIVITY_SERVICE
    )as ConnectivityManager)

    private val networkCallback = object:ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }
}