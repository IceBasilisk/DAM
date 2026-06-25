package a47514.masterplanner.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Monitors the network connectivity state.
 */
object NetworkMonitor {
    /**
     * Returns a flow of network connectivity status (boolean).
     */
    fun observe(context: Context): Flow<Boolean> = callbackFlow {
        /**
         * Android's network manager service. Knows the type of connectivity (WIFI/mobile data).
         */
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        /**
         * Callback for network changes (true for active connection, false otherwise).
         */
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { trySend(true) }
            override fun onLost(network: Network) { trySend(false) }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // Register the callback with the connectivity manager
        connectivityManager.registerNetworkCallback(request, callback)

        // Emit current state immediately
        val current = connectivityManager.activeNetwork?.let {
            connectivityManager
                .getNetworkCapabilities(it)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } ?: false
        trySend(current)

        // Unregister the callback when the flow is closed, prevents memory leaks
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}