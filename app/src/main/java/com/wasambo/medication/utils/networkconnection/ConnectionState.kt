package com.wasambo.medication.utils.networkconnection

sealed class ConnectionState {
    data object Available : ConnectionState()
    data object Unavailable : ConnectionState()
}