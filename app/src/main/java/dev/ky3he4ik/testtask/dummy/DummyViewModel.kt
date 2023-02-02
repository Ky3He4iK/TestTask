package dev.ky3he4ik.testtask.dummy

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ky3he4ik.testtask.navigation.RouteNavigator
import javax.inject.Inject

@HiltViewModel
class DummyViewModel @Inject constructor(private val routeNavigator: RouteNavigator) :
    ViewModel(), RouteNavigator by routeNavigator
