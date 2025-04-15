package com.example.offlinevotehotspot.network

import android.util.Log
import com.example.offlinevotehotspot.model.Vote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

class VoteServer(private val port: Int = 6969) {
    private var serverSocket: ServerSocket? = null
    private val votes = mutableListOf<Vote>()

    suspend fun startListening(onVoteReceived: (Vote) -> Unit) = withContext(Dispatchers.IO) {
        try {
            serverSocket = ServerSocket(port)
            Log.d("VoteServer", "Server started on port $port")

            while (true) {
                val clientSocket = serverSocket!!.accept()
                handleClient(clientSocket, onVoteReceived)
            }
        } catch (e: Exception) {
            Log.e("VoteServer", "Error: ${e.message}")
        }
    }

    private fun handleClient(socket: Socket, onVoteReceived: (Vote) -> Unit) {
        try {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val json = reader.readLine()
            val obj = JSONObject(json)
            val vote = Vote(
                deviceName = obj.getString("deviceName"),
                candidateId = obj.getString("candidateId")
            )
            votes.add(vote)

            // Switch to main thread for UI updates
            CoroutineScope(Dispatchers.Main).launch {
                onVoteReceived(vote)
            }

            socket.close()
        } catch (e: Exception) {
            Log.e("VoteServer", "Error reading vote: ${e.message}")
        }
    }

    fun stop() {
        serverSocket?.close()
    }

    fun getVotes(): List<Vote> = votes
}