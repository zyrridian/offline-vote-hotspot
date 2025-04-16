package com.example.offlinevotehotspot.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.offlinevotehotspot.data.repository.VoteRepositoryImpl
import com.example.offlinevotehotspot.domain.usecase.VoteHandler
import com.example.offlinevotehotspot.presentation.screens.HostScreen
import com.example.offlinevotehotspot.presentation.screens.PeerDiscoveryScreen
import com.example.offlinevotehotspot.presentation.screens.RoleSelectionScreen
import com.example.offlinevotehotspot.presentation.screens.VoterScreen

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    val voteHandler = remember {
        VoteHandler(VoteRepositoryImpl())
    }

    NavHost(navController = navController, startDestination = "role_selection") {
        composable("role_selection") {
            RoleSelectionScreen(navController)
        }
        composable("peer_discovery") {
            PeerDiscoveryScreen(
                navController = navController
            )
        }
        composable("host") {
            HostScreen(
                voteHandler = voteHandler
            )
        }
        composable("voter") {
            VoterScreen()
        }
    }
}
