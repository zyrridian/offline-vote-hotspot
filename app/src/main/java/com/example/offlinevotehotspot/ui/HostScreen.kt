package com.example.offlinevotehotspot.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.offlinevotehotspot.model.Vote
import com.example.offlinevotehotspot.network.VoteServer
import kotlinx.coroutines.launch

@Composable
fun HostScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val voteList = remember { mutableStateListOf<Vote>() }
    val voteServer = remember { VoteServer() }

    LaunchedEffect(Unit) {
//        voteList.addAll(voteServer.getVotes())
        scope.launch {
            voteServer.startListening {
                Log.d("HostScreen", "Received vote from ${it.deviceName}")
                voteList.add(it)
            }
        }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(voteList) {
            Text("${it.deviceName} voted for ${it.candidateId}")
        }
    }
}
