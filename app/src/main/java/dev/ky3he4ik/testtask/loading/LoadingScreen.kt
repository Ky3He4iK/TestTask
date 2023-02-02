package dev.ky3he4ik.testtask.loading

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ky3he4ik.testtask.R
import dev.ky3he4ik.testtask.navigation.NavRoute

object LoadingRoute : NavRoute<LoadingViewModel> {
    override val route = "loading/"

    @Composable
    override fun viewModel(): LoadingViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: LoadingViewModel) = LoadingScreen(viewModel)
}

@Composable
private fun LoadingScreen(viewModel: LoadingViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Loading...",
        )
    }
    viewModel.init()
}
