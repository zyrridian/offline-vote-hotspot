package com.example.offlinevotehotspot.presentation.screens

import android.os.Build
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.offlinevotehotspot.data.model.Vote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.Socket

@Composable
fun VoterScreen() {
    var vote by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Voter Screen", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = vote,
            onValueChange = { vote = it },
            label = { Text("Your Vote") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val socket = Socket("192.168.49.1", 8888) // Replace with actual GO IP
                    val output: OutputStream = socket.getOutputStream()
                    output.write(vote.toByteArray())
                    output.flush()
                    socket.close()
                    status = "Vote sent!"
                } catch (e: Exception) {
                    status = "Failed to send vote: ${e.message}"
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Send Vote")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(status)
    }
}
