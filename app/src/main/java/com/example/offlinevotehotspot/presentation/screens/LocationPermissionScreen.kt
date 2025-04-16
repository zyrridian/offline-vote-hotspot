package com.example.offlinevotehotspot.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionScreen(onPermissionGranted: () -> Unit) {
    val permissionState = rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    when {
        permissionState.status.isGranted -> {
            // Permission granted
            onPermissionGranted()
        }

        permissionState.status.shouldShowRationale -> {
            Text("Location permission is needed for discovering nearby devices.")
        }

        else -> {
            Text("Please grant location permission in settings.")
        }
    }
}
