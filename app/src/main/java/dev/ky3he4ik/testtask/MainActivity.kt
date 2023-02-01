package dev.ky3he4ik.testtask

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.edit
import androidx.preference.Preference
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dev.ky3he4ik.testtask.ui.theme.TestTaskTheme
import dev.ky3he4ik.testtask.util.Utils

const val URL_KEY_PREFS = "url"
const val URL_KEY_FIREBASE = "url"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoadingScreen()
        }

        val preferences = Preference(this).sharedPreferences
        val storedUrl = preferences?.getString(URL_KEY_PREFS, null)
        if (storedUrl == null) {
//            TODO: Check internet, show webview or no internet
        } else {
//            TODO: connect to firebase; get url
            try {
                val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
                val configSettings = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
                remoteConfig.setConfigSettingsAsync(configSettings)
                remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result) {
                        val firebaseUrl = remoteConfig.getString(URL_KEY_FIREBASE)

                        if (Utils.isShowDummy(this, firebaseUrl)) {
//                      TODO: show dummy
                        } else {
                            preferences.edit(commit = true) {
                                putString(URL_KEY_PREFS, firebaseUrl)
                            }
                            //TODO: show webview
                        }
                    } else {
//                        TODO: show error
                    }
                }
            } catch (e: FirebaseRemoteConfigException) {
//                TODO: show error screen
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    TestTaskTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Loading...",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoadingScreen()
}
