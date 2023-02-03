package dev.ky3he4ik.testtask.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.ky3he4ik.testtask.workouts.WorkoutsRoute
import dev.ky3he4ik.testtask.error.ErrorRoute
import dev.ky3he4ik.testtask.loading.LoadingRoute
import dev.ky3he4ik.testtask.web.WebRoute

@Composable
fun NavigationComponent(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = LoadingRoute.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        LoadingRoute.composable(this, navHostController)
        ErrorRoute.composable(this, navHostController)
        WorkoutsRoute.composable(this, navHostController)
        WebRoute.composable(this, navHostController)
    }
}