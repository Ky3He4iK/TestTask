package dev.ky3he4ik.testtask.web

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ky3he4ik.testtask.navigation.RouteNavigator
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val routeNavigator: RouteNavigator
) :
    ViewModel(), RouteNavigator by routeNavigator {
    private val url = WebRoute.getUrlMessage(savedStateHandle)

    var state by mutableStateOf(url)
}
