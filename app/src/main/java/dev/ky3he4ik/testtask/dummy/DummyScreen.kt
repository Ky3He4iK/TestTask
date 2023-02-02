package dev.ky3he4ik.testtask.dummy

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ky3he4ik.testtask.navigation.NavRoute


object DummyRoute : NavRoute<DummyViewModel> {
    override val route = "dummy/"

    @Composable
    override fun viewModel(): DummyViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: DummyViewModel) = DummyScreen()
}

@Composable
private fun DummyScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(text = "Here will be smth")
//        TODO()
    }
}
