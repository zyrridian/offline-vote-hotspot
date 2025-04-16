package com.example.offlinevotehotspot.utils

import android.content.Context
import android.net.wifi.WifiManager
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.nio.ByteBuffer

fun getLocalIp(context: Context): String {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val ipAddress = wifiManager.connectionInfo.ipAddress
    val ipBytes = ByteBuffer.allocate(4).putInt(ipAddress).array().reversedArray()
    return InetAddress.getByAddress(ipBytes).hostAddress ?: "Unavailable"
}

fun getLocalIpAddress(): String? {
    val interfaces = NetworkInterface.getNetworkInterfaces()
    for (intf in interfaces) {
        val addrs = intf.inetAddresses
        for (addr in addrs) {
            if (!addr.isLoopbackAddress && addr is Inet4Address) {
                return addr.hostAddress
            }
        }
    }
    return null
}

fun getGatewayIpAddress(context: Context): String? {
    val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val dhcpInfo = wifiManager.dhcpInfo ?: return null

    val gatewayInt = dhcpInfo.gateway
    return String.format(
        "%d.%d.%d.%d",
        gatewayInt and 0xFF,
        gatewayInt shr 8 and 0xFF,
        gatewayInt shr 16 and 0xFF,
        gatewayInt shr 24 and 0xFF
    )
}
