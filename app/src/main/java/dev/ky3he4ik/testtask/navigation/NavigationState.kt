package dev.ky3he4ik.testtask.navigation

sealed class NavigationState {
    object Idle : NavigationState()
    data class NavigateToRoute(val route: String) : NavigationState()
}
