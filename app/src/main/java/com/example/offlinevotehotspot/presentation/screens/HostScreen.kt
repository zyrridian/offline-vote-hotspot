package com.example.offlinevotehotspot.presentation.screens

import android.content.Context
import android.net.wifi.p2p.WifiP2pManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.offlinevotehotspot.data.model.Vote
import com.example.offlinevotehotspot.domain.usecase.VoteHandler
import com.example.offlinevotehotspot.network.socket.ServerSocketHandler
import com.example.offlinevotehotspot.network.wifi.WifiP2PHelper

@Composable
fun HostScreen(voteHandler: VoteHandler) {
    val context = LocalContext.current
    var votes by remember { mutableStateOf(listOf<Vote>()) }
    var question by remember { mutableStateOf("") }
    var options by remember { mutableStateOf("") }
    var isVotingStarted by remember { mutableStateOf(false) }
    var connectionStatus by remember { mutableStateOf("Setting up host...") }

    LaunchedEffect(Unit) {
        setupWiFiDirect(context) { status ->
            connectionStatus = status
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Host Vote Session",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isVotingStarted) {
            OutlinedTextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("Question") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = options,
                onValueChange = { options = it },
                label = { Text("Options (comma separated)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (question.isNotBlank() && options.isNotBlank()) {
                        isVotingStarted = true
                        startVoteSession()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = question.isNotBlank() && options.isNotBlank()
            ) {
                Text("Start Vote Session")
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = question,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Options: ${options.split(",").joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = connectionStatus,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Votes Received",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(votes) { vote ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(vote.voterName)
                            Text(vote.choice)
                        }
                    }
                }
            }
        }
    }
}

private fun setupWiFiDirect(context: Context, onStatusUpdate: (String) -> Unit) {
    val manager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    val channel = manager.initialize(context, context.mainLooper, null)
    val wifiP2PHelper = WifiP2PHelper(context, manager, channel)

    wifiP2PHelper.discoverPeers { peers ->
        onStatusUpdate("Found ${peers.size} peers")
    }
}

private fun startVoteSession() {
    // Start server socket to receive votes
    val server = ServerSocketHandler { rawVote ->
        // Handle incoming votes
    }
    server.startServer()
}


