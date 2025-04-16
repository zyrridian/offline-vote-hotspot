package com.example.offlinevotehotspot.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RoleSelectionScreen(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Offline Vote",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose your role",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        ElevatedButton(
            onClick = { navController.navigate("host") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Create a Vote Session (Host)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        ElevatedButton(
            onClick = { navController.navigate("peer_discovery") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Join a Vote Session (Voter)")
        }
    }
}
