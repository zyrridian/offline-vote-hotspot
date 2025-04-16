package com.example.offlinevotehotspot

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.offlinevotehotspot.network.wifi.WifiBroadcastReceiver
import com.example.offlinevotehotspot.presentation.navigation.AppNavGraph
import com.example.offlinevotehotspot.ui.theme.OfflineVoteHotspotTheme

class MainActivity : ComponentActivity() {

    private lateinit var manager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel
    private lateinit var receiver: WifiBroadcastReceiver

    private val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    // Permission launcher
    private val permissionLauncher = registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            setupWifiP2P()
        } else {
            Toast.makeText(this, "Permissions are required for Wi-Fi Direct", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request permissions if not granted
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        setContent {
            OfflineVoteHotspotTheme {
                AppNavGraph()
            }
        }
    }

    private fun setupWifiP2P() {
        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, mainLooper, null)

        receiver = WifiBroadcastReceiver(manager, channel) { peerList ->
            // handled via composable
        }
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OfflineVoteHotspotTheme {

    }
}