package com.example.offlinevotehotspot.network

import android.util.Log
import com.example.offlinevotehotspot.model.Vote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.Socket

class VoteClient(private val hostIp: String, private val port: Int = 6969) {
    suspend fun sendVote(vote: Vote) = withContext(Dispatchers.IO) {
        try {
            val socket = Socket(hostIp, port)
            val writer = OutputStreamWriter(socket.getOutputStream())

            val json = JSONObject().apply {
                put("deviceName", vote.deviceName)
                put("candidateId", vote.candidateId)
            }

            writer.write(json.toString())
            writer.flush()
            socket.close()

            Log.d("VoteClient", "Vote sent: $json")
        } catch (e: Exception) {
            Log.e("VoteClient", "Error handling vote: ${e.message}")
        }
    }
}