package dev.ky3he4ik.testtask.navigation

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Navigator to use when initiating navigation from a ViewModel.
 */
interface RouteNavigator {
    fun onNavigated(state: NavigationState)
    fun navigateToRoute(route: String)
    fun navigateBack()
    val navigationState: StateFlow<NavigationState>
}

class RouteNavigatorImpl : RouteNavigator {
    override val navigationState: MutableStateFlow<NavigationState> =
        MutableStateFlow(NavigationState.Idle)

    override fun onNavigated(state: NavigationState) {
        // clear navigation state, if state is the current state:
        navigationState.compareAndSet(state, NavigationState.Idle)
    }

    override fun navigateToRoute(route: String) = navigate(NavigationState.NavigateToRoute(route))

    override fun navigateBack() = navigate(NavigationState.NavigateBack)

    @VisibleForTesting
    fun navigate(state: NavigationState) {
        navigationState.value = state
    }
}
