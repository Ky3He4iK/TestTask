package dev.ky3he4ik.testtask

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface NavDestinations {
    val route: String
}

object ShowError: NavDestinations {
    override val route = "error"
    const val errorArg = "error_message"
    val arguments = listOf(
        navArgument(errorArg) { type = NavType.StringType },
    )
}

object ShowWeb: NavDestinations {
    override val route = "web"
    const val urlArg = "url"
    val arguments = listOf(
        navArgument(urlArg) { type = NavType.StringType },
    )
}

object ShowDummy: NavDestinations {
    override val route = "dummy"
}

object ShowLoading: NavDestinations {
    override val route = "loading"
}
