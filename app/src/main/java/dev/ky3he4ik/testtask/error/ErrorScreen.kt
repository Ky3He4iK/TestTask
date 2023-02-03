package dev.ky3he4ik.testtask.error

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.ky3he4ik.testtask.R
import dev.ky3he4ik.testtask.navigation.NavRoute
import dev.ky3he4ik.testtask.util.Utils

const val KEY_ERROR_MESSAGE = "error"

object ErrorRoute : NavRoute<ErrorViewModel> {
    override val route = "error/{$KEY_ERROR_MESSAGE}/"

    fun get(error: String): String = route.replace("{$KEY_ERROR_MESSAGE}", Utils.urlEncode(error))

    @Composable
    override fun viewModel(): ErrorViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: ErrorViewModel) = ErrorScreen(viewModel)

    override fun getArguments(): List<NamedNavArgument> =
        listOf(navArgument(KEY_ERROR_MESSAGE) { type = NavType.StringType })

    fun getErrorMessage(savedStateHandle: SavedStateHandle) =
        Utils.urlDecode(savedStateHandle.get<String>(KEY_ERROR_MESSAGE))
}

@Composable
private fun ErrorScreen(viewModel: ErrorViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_warning_24),
                contentDescription = "Error icon",
                modifier = Modifier.defaultMinSize(120.dp, 120.dp)
            )
            Text(
                text = "Error: ${viewModel.state ?: "Unknown error"}",
                fontSize = 32.sp,
                overflow = TextOverflow.Visible, textAlign = TextAlign.Center
            )
        }
    }
}
