package com.example.offlinevotehotspot.network.socket

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.offlinevotehotspot.data.model.Vote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Socket

class ClientSocketHandler(
    private val context: Context,
    private val mainLooper: Looper,
    private val port: Int = 6969
) {

    private var wifiP2pManager: WifiP2pManager? = null
    private var wifiP2pChannel: WifiP2pManager.Channel? = null

    suspend fun startListening(onVoteReceived: (Vote) -> Unit) = withContext(Dispatchers.IO) {
        try {
            // Initialize Wi-Fi Direct manager and start discovery
            wifiP2pManager = context.getSystemService(WifiP2pManager::class.java)
            wifiP2pChannel = wifiP2pManager!!.initialize(context, mainLooper, null)

            // Permission check
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("ClientSocket", "Location permission not granted")
                return@withContext
            }

            wifiP2pManager?.discoverPeers(wifiP2pChannel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.d("VoteServer", "Wi-Fi Direct discovery started.")
                }

                override fun onFailure(reason: Int) {
                    Log.e("VoteServer", "Wi-Fi Direct discovery failed: $reason")
                }
            })

            // The rest of the server setup logic, e.g., handling client connections, goes here...
        } catch (e: SecurityException) {
            Log.e("ClientSocket", "SecurityException: ${e.message}")
        } catch (e: Exception) {
            Log.e("ClientSocket", "Error: ${e.message}")
        }
    }

    // Handle incoming votes over Wi-Fi Direct once a connection is established
    private fun handleClient(socket: Socket, onVoteReceived: (Vote) -> Unit) {
        // Handle reading vote data here
    }
}

// compose
//val handler = ClientSocketHandler(
//    context = LocalContext.current,
//    mainLooper = Looper.getMainLooper()
//)

// outside compose
//val handler = ClientSocketHandler(
//    context = this,
//    mainLooper = mainLooper
//)