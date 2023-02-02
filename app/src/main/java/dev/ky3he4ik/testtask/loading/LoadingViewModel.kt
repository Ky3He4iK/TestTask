package dev.ky3he4ik.testtask.loading

import android.app.Application
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.Preference
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ky3he4ik.testtask.dummy.DummyRoute
import dev.ky3he4ik.testtask.error.ErrorRoute
import dev.ky3he4ik.testtask.navigation.RouteNavigator
import dev.ky3he4ik.testtask.util.Utils
import dev.ky3he4ik.testtask.web.WebRoute
import kotlinx.coroutines.launch
import javax.inject.Inject

const val URL_KEY_PREFS = "url"
const val URL_KEY_FIREBASE = "url"

@HiltViewModel
class LoadingViewModel @Inject constructor(
    private val routeNavigator: RouteNavigator,
    application: Application
) :
    AndroidViewModel(application), RouteNavigator by routeNavigator {

    fun init() {
        viewModelScope.launch {
            val context = getApplication<Application>()
            val preferences = Preference(context).sharedPreferences
            val storedUrl = preferences?.getString(URL_KEY_PREFS, null)
            if (storedUrl.isNullOrEmpty()) {
                try {
                    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
                    val configSettings = remoteConfigSettings {
                        minimumFetchIntervalInSeconds = 3600
                    }
                    remoteConfig.setConfigSettingsAsync(configSettings)
                    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUrl = remoteConfig.getString(URL_KEY_FIREBASE)
                            if (firebaseUrl.isBlank() || Utils.isTestScenario(context)) {
                                navigateToRoute(DummyRoute.route)
                                return@addOnCompleteListener
                            }
                            preferences?.edit(commit = true) {
                                putString(URL_KEY_PREFS, firebaseUrl)
                            }
                            navigateToRoute(WebRoute.get(firebaseUrl))
                            return@addOnCompleteListener

                        }
                        navigateToRoute(
                            ErrorRoute.get(
                                task.exception?.message ?: "Can't get url"
                            )
                        )
                    }
                } catch (e: FirebaseRemoteConfigException) {
                    e.printStackTrace()
                    navigateToRoute(ErrorRoute.get(e.message ?: "Unknown error"))
                }
                return@launch
            }
            if (Utils.hasInternet(context)) {
                navigateToRoute(WebRoute.get(storedUrl))
            } else
                navigateToRoute(ErrorRoute.get("No internet"))
        }
    }
}
