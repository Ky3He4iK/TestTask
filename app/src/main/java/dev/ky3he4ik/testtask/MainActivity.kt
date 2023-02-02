package dev.ky3he4ik.testtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.ky3he4ik.testtask.navigation.NavigationComponent
import dev.ky3he4ik.testtask.ui.theme.TestTaskTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            TestTaskTheme {
                Scaffold {
                    NavigationComponent(navController, it)
                }
            }
        }
    }
}
