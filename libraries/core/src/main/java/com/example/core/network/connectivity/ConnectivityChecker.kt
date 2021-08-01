package com.example.core.network.connectivity

/**
 * Interface for hiding network connectivity resolution
 */
interface ConnectivityChecker {

    fun isConnected(): Boolean
}
