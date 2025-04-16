package com.example.offlinevotehotspot.network.socket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket

class ServerSocketHandler(private val onVoteReceived: (String) -> Unit) {
    private val serverScope = CoroutineScope(Dispatchers.IO)

    fun startServer(port: Int = 8888) {
        serverScope.launch {
            try {
                val serverSocket = ServerSocket(port)
                while (true) {
                    val client = serverSocket.accept()
                    val reader = BufferedReader(InputStreamReader(client.getInputStream()))
                    val vote = reader.readLine()
                    onVoteReceived(vote)
                    client.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
