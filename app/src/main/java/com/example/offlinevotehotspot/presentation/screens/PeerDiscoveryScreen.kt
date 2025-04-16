package com.example.offlinevotehotspot.presentation.screens


import android.Manifest
import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import android.widget.Toast
import androidx.navigation.NavController

@Composable
fun PeerDiscoveryScreen(navController: NavController) {
    val context = LocalContext.current
    val manager = remember {
        context.getSystemService(WifiP2pManager::class.java)
    }
    val channel = remember {
        manager?.initialize(context, context.mainLooper, null)
    }

    // Check permissions for Wi-Fi Direct
    val isLocationPermissionGranted = remember {
        ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    var peers by remember { mutableStateOf<List<WifiP2pDevice>>(emptyList()) }

    LaunchedEffect(Unit) {
        if (isLocationPermissionGranted) {
            manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Toast.makeText(context, "Discovery started", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(reason: Int) {
                    Toast.makeText(context, "Discovery failed: $reason", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(context, "Location permission required", Toast.LENGTH_SHORT).show()
        }
    }

    // UI to show peer list
    Column(Modifier.padding(16.dp)) {
        Text("Nearby Devices", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        if (peers.isEmpty()) {
            Text("No devices found yet.")
        } else {
            peers.forEach { device ->
                Text(
                    text = "${device.deviceName} (${device.deviceAddress})",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Toast
                                .makeText(context, "Selected: ${device.deviceName}", Toast.LENGTH_SHORT)
                                .show()
                            // Optionally navigate
                            // navController.navigate("voter")
                        }
                        .padding(8.dp)
                )
            }
        }
    }

    // You'd update `peers` via a broadcast receiver listening to WIFI_P2P_PEERS_CHANGED_ACTION
}

