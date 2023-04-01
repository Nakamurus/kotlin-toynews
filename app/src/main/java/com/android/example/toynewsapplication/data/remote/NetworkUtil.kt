package com.android.example.toynewsapplication.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkUtil {

    @Suppress("DEPRECATION")
    fun isInternetAvailable(context: Context?): Boolean {
        context?.let {
            val connectivityManager =
                it.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager?.run {
                    connectivityManager.getNetworkCapabilities(activeNetwork)?.run {
                        return hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(
                            NetworkCapabilities.TRANSPORT_CELLULAR
                        )
                    }
                }
            } else {
                connectivityManager?.run {
                    @Suppress("DEPRECATION")
                    return activeNetworkInfo?.isConnected == true
                }
            }
        }
        return false
    }
}