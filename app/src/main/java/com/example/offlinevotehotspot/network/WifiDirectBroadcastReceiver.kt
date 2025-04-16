package com.example.offlinevotehotspot.network

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.p2p.*
import android.util.Log
import androidx.core.content.ContextCompat

class WifiBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val onPeersAvailable: (List<WifiP2pDevice>) -> Unit,
    private val onConnectionInfoAvailable: ((WifiP2pInfo) -> Unit)? = null
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Log.d("WifiP2P", "Wifi P2P is enabled")
                } else {
                    Log.e("WifiP2P", "Wifi P2P is not enabled")
                }
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                context?.let {
                    if (ContextCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        manager.requestPeers(channel) { peers: WifiP2pDeviceList ->
                            onPeersAvailable(peers.deviceList.toList())
                        }
                    } else {
                        Log.w("WifiP2P", "Permission denied: Cannot request peers")
                    }
                }
            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                context?.let {
                    if (ContextCompat.checkSelfPermission(
                            it,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        manager.requestConnectionInfo(channel) { info: WifiP2pInfo ->
                            onConnectionInfoAvailable?.invoke(info)
                            if (info.groupFormed) {
                                if (info.isGroupOwner) {
                                    Log.d("WifiP2P", "Connected as group owner")
                                } else {
                                    Log.d("WifiP2P", "Connected as peer")
                                }
                            } else {
                                Log.d("WifiP2P", "Group not formed")
                            }
                        }
                    } else {
                        Log.w("WifiP2P", "Permission denied: Cannot request connection info")
                    }
                }
            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                val device: WifiP2pDevice? = intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE
                )
                device?.let {
                    Log.d("WifiP2P", "Device status: ${getDeviceStatus(it.status)}")
                }
            }
        }
    }

    private fun getDeviceStatus(deviceStatus: Int): String = when (deviceStatus) {
        WifiP2pDevice.AVAILABLE -> "Available"
        WifiP2pDevice.INVITED -> "Invited"
        WifiP2pDevice.CONNECTED -> "Connected"
        WifiP2pDevice.FAILED -> "Failed"
        WifiP2pDevice.UNAVAILABLE -> "Unavailable"
        else -> "Unknown"
    }
}
