package dev.ky3he4ik.testtask.web

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
        val siteUrl = viewModel.state
        if (siteUrl == null) {
            viewModel.navigateToRoute(ErrorRoute.get("No url passed"))
            return@Surface
        }
        val context = LocalContext.current
        val webView =
            rememberSaveable(LocalContext.current, stateSaver = getWebViewSaver(context)) {
                mutableStateOf(initWebView(context, null, siteUrl))
            }

        AndroidView(factory = {
            webView.value
        }, update = {
            it.loadUrl(siteUrl)
        })
    }
}

fun getWebViewSaver(context: Context) = run {
    Saver<WebView, Bundle>(save = {
        val bundle = Bundle()
        it.saveState(bundle)
        bundle
    }, restore = {
        initWebView(context, it, "")
    })
}


private fun initWebView(context: Context, savedInstanceState: Bundle?, url: String) =
    WebView(context).apply {
        webViewClient = WebViewClient()
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            useWideViewPort = true
            domStorageEnabled = true
            databaseEnabled = true
            setSupportZoom(false)
            allowFileAccess = true
            allowContentAccess = true
            loadWithOverviewMode = true
        }
        CookieManager.getInstance().setAcceptCookie(true)
        if (savedInstanceState != null)
            restoreState(savedInstanceState)
        else
            loadUrl(url)
    }
