package com.example.offlinevotehotspot.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RoleSelectionScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("host") }, modifier = Modifier.fillMaxWidth()) {
            Text("Host")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("voter") }, modifier = Modifier.fillMaxWidth()) {
            Text("Voter")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("peer_discovery") }, modifier = Modifier.fillMaxWidth()) {
            Text("Peer Discovery")
        }
    }
}
