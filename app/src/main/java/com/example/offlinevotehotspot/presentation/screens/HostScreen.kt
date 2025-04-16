package com.example.offlinevotehotspot.presentation.screens

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

@Composable
fun HostScreen(voteHandler: VoteHandler) {
    var votes by remember { mutableStateOf(listOf<Vote>()) }

    LaunchedEffect(Unit) {
        val server = ServerSocketHandler { rawVote ->
            val success = voteHandler.handleVote(rawVote)
            if (success) {
                votes = voteHandler.getResults()
            }
        }
        server.startServer()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Host: Votes Received", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        if (votes.isEmpty()) {
            Text("No votes received yet.")
        } else {
            votes.forEach { vote ->
                Text("${vote.voterName} voted for: ${vote.choice}")
            }
        }
    }

    // TODO: Setup Wi-Fi P2P and server socket to receive votes
}

