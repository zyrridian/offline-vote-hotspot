package com.example.offlinevotehotspot.ui

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.offlinevotehotspot.model.Vote
import com.example.offlinevotehotspot.network.VoteClient
import kotlinx.coroutines.launch

@Composable
fun ClientScreen(
    hostIp: String,
    onVoted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val candidateList = listOf("Candidate A", "Candidate B", "Candidate C")

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Choose your vote:")
        candidateList.forEach { candidate ->
            Button(
                onClick = {
                    scope.launch {
                        val vote = Vote(
                            deviceName = Build.MODEL,
                            candidateId = candidate
                        )
                        VoteClient(hostIp).sendVote(vote)
                        onVoted()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(candidate)
            }
        }
    }
    
}