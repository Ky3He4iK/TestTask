package dev.ky3he4ik.testtask.web

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import dev.ky3he4ik.testtask.error.ErrorRoute
import dev.ky3he4ik.testtask.navigation.NavRoute
import dev.ky3he4ik.testtask.util.Utils


const val KEY_URL_MESSAGE = "url"

object WebRoute : NavRoute<WebViewModel> {
    override val route = "web/{$KEY_URL_MESSAGE}/"

    fun get(url: String): String = route.replace("{$KEY_URL_MESSAGE}", Utils.urlEncode(url))

    @Composable
    override fun viewModel(): WebViewModel = hiltViewModel()

    @Composable
    override fun Content(viewModel: WebViewModel) = WebScreen(viewModel)

    override fun getArguments(): List<NamedNavArgument> =
        listOf(navArgument(KEY_URL_MESSAGE) { type = NavType.StringType })

    fun getUrlMessage(savedStateHandle: SavedStateHandle) =
        Utils.urlDecode(savedStateHandle.get<String>(KEY_URL_MESSAGE))
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebScreen(viewModel: WebViewModel) {
    BackHandler(true) {
        // disable back button
    }

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {


        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
//                    settings.

                val siteUrl = viewModel.state
                if (siteUrl == null) {
                    viewModel.navigateToRoute(ErrorRoute.get("No url passed"))
                } else
                    loadUrl(siteUrl)
            }
        }
//                , update = {
//                it.loadUrl(url)
//            }
        )
    }
}
