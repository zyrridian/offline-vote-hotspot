package com.example.offlinevotehotspot.utils

import android.content.Context
import android.net.wifi.WifiManager
import java.net.InetAddress
import java.nio.ByteBuffer

fun getLocalIp(context: Context): String {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val ipAddress = wifiManager.connectionInfo.ipAddress
    val ipBytes = ByteBuffer.allocate(4).putInt(ipAddress).array().reversedArray()
    return InetAddress.getByAddress(ipBytes).hostAddress ?: "Unavailable"
}
