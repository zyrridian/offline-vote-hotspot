package com.example.offlinevotehotspot.presentation.screens

import android.content.Context
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.net.Socket
import com.example.offlinevotehotspot.network.wifi.WifiP2PHelper

@Composable
fun VoterScreen() {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf("") }
    var voterName by remember { mutableStateOf("") }
    var connectionStatus by remember { mutableStateOf("Searching for host...") }
    var availableHosts by remember { mutableStateOf(listOf<WifiP2pDevice>()) }
    var selectedHost by remember { mutableStateOf<WifiP2pDevice?>(null) }
    var isConnected by remember { mutableStateOf(false) }
    var voteStatus by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        setupWiFiDirect(context,
            onStatusUpdate = { status -> connectionStatus = status },
            onPeersFound = { peers -> availableHosts = peers }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Join Vote Session",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isConnected) {
            Text(
                text = connectionStatus,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (availableHosts.isNotEmpty()) {
                Text(
                    text = "Available Hosts",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(availableHosts) { host ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedHost = host
                                    connectToHost(context, host) { connected ->
                                        isConnected = connected
                                    }
                                }
                        ) {
                            Text(
                                text = host.deviceName,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            } else {
                Text("No hosts found. Please wait...")
            }
        } else {
            OutlinedTextField(
                value = voterName,
                onValueChange = { voterName = it },
                label = { Text("Your Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = selectedOption,
                onValueChange = { selectedOption = it },
                label = { Text("Your Vote") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (voterName.isNotBlank() && selectedOption.isNotBlank()) {
                        submitVote(voterName, selectedOption) { status ->
                            voteStatus = status
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = voterName.isNotBlank() && selectedOption.isNotBlank()
            ) {
                Text("Submit Vote")
            }

            if (voteStatus.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = voteStatus,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (voteStatus.contains("success", ignoreCase = true))
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private fun setupWiFiDirect(
    context: Context,
    onStatusUpdate: (String) -> Unit,
    onPeersFound: (List<WifiP2pDevice>) -> Unit
) {
    val manager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    val channel = manager.initialize(context, context.mainLooper, null)
    val wifiP2PHelper = WifiP2PHelper(context, manager, channel)

    wifiP2PHelper.discoverPeers { peers ->
        onStatusUpdate("Found ${peers.size} hosts")
        onPeersFound(peers)
    }
}

private fun connectToHost(
    context: Context,
    host: WifiP2pDevice,
    onConnected: (Boolean) -> Unit
) {
    val manager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    val channel = manager.initialize(context, context.mainLooper, null)
    val wifiP2PHelper = WifiP2PHelper(context, manager, channel)

    wifiP2PHelper.connectToDevice(host)
    // In a real app, you'd wait for the connection callback
    onConnected(true)
}

private fun submitVote(
    voterName: String,
    choice: String,
    onVoteStatus: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val vote = "$voterName:$choice"
            val socket = Socket("192.168.49.1", 8888) // Replace with actual host IP
            val output: OutputStream = socket.getOutputStream()
            output.write(vote.toByteArray())
            output.flush()
            socket.close()
            onVoteStatus("Vote submitted successfully!")
        } catch (e: Exception) {
            onVoteStatus("Failed to submit vote: ${e.message}")
        }
    }
}
