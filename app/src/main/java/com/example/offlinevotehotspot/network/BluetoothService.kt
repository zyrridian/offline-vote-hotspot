package com.example.offlinevotehotspot.network
//
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothSocket
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.LocationManager
//import android.os.Build
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import java.io.OutputStream
//import java.util.UUID
//
//class BluetoothService(private val context: Context) {
//
//    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
//
//    // UUID for your Bluetooth service (must match server's UUID)
//    private val uuid = UUID.fromString("YOUR_UUID")
//
//    // Explicit permission check function for Bluetooth and location permissions
//    fun checkPermissions(): Boolean {
//        // Check Bluetooth permissions
//        val isBluetoothPermissionGranted = ContextCompat.checkSelfPermission(
//            context, android.Manifest.permission.BLUETOOTH
//        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
//            context, android.Manifest.permission.BLUETOOTH_ADMIN
//        ) == PackageManager.PERMISSION_GRANTED
//
//        // Check location permissions for device discovery (required on Android 6.0+)
//        val isLocationPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
//                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//        } else {
//            true  // Location permissions are not required on pre-Android 6.0
//        }
//
//        // If all permissions are granted, return true
//        return isBluetoothPermissionGranted && isLocationPermissionGranted
//    }
//
//    // Check if Bluetooth is enabled and if the device supports Bluetooth
//    fun isBluetoothEnabled(): Boolean {
//        return bluetoothAdapter != null && bluetoothAdapter.isEnabled
//    }
//
//    // Send the vote to the first bonded Bluetooth device, with explicit permission check
//    fun sendVote(vote: String) {
//        // Check if all required permissions are granted
//        if (!checkPermissions()) {
//            println("Permissions not granted. Unable to proceed with sending vote.")
//            return
//        }
//
//        // Check if Bluetooth is enabled
//        if (!isBluetoothEnabled()) {
//            println("Bluetooth is not enabled. Please enable Bluetooth.")
//            return
//        }
//
//        // Proceed to send the vote if permissions are granted and Bluetooth is enabled
//        val device: BluetoothDevice? = bluetoothAdapter?.bondedDevices?.firstOrNull()
//
//        // Explicit check to ensure device is not null before proceeding
//        device?.let { connectedDevice ->
//            try {
//                val socket: BluetoothSocket = connectedDevice.createRfcommSocketToServiceRecord(uuid)
//                socket.connect()
//
//                val outputStream: OutputStream = socket.outputStream
//                outputStream.write(vote.toByteArray())
//                outputStream.flush()
//
//                socket.close()
//                println("Vote sent successfully!")
//            } catch (e: SecurityException) {
//                // Handle permission-related exceptions
//                e.printStackTrace()
//                println("SecurityException: Insufficient permissions to access Bluetooth.")
//            } catch (e: Exception) {
//                // Handle other types of exceptions
//                e.printStackTrace()
//                println("Error while sending vote: ${e.message}")
//            }
//        } ?: run {
//            // Handle case where no bonded device is found
//            println("No paired Bluetooth devices found.")
//        }
//    }
//}
