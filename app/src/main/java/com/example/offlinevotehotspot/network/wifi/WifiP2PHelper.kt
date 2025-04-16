package com.example.offlinevotehotspot.network.wifi

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.content.Context
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log

class WifiP2PHelper(
    private val context: Context,
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel
) {
    fun connectToDevice(device: WifiP2pDevice) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("WiFiP2P", "Location permission not granted")
            return
        }

        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
        }

        manager.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d("WiFiP2P", "Connection initiated")
            }

            override fun onFailure(reason: Int) {
                Log.e("WiFiP2P", "Connection failed: $reason")
            }
        })
    }

    fun discoverPeers(onPeersAvailable: (List<WifiP2pDevice>) -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("WiFiP2P", "Location permission not granted")
            return
        }

        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d("WiFiP2P", "Discovery started")
            }

            override fun onFailure(reason: Int) {
                Log.e("WiFiP2P", "Discovery failed: $reason")
            }
        })

        // This assumes a registered broadcast receiver will call `onPeersAvailable`
    }
}
