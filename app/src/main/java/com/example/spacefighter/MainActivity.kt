package com.example.spacefighter

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spacefighter.ui.theme.SpaceFighterTheme

class MainActivity : ComponentActivity() {

    private val score = Score()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            var currentScore by remember { mutableStateOf(score.points) }
            var gameOver by remember { mutableStateOf(false) }

            SpaceFighterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    NavHost(navController = navController,
                        startDestination = "game_start"){
                        composable("game_start"){
                            GameHomeView(onPlayClick = {
                                navController.navigate("game_screen")
                            })
                        }
                        composable("game_screen") {
                            GameScreenView(score) {
                                currentScore = score.points
                                if (gameOver) {
                                    navController.navigate("game_over")
                                }
                            }
                        }
                        composable("game_over") {
                            GameOverView(onRestartClick = {
                                score.reset()
                                gameOver = false
                                navController.navigate("game_screen")
                            })
                        }
                    }
                    Text(
                        text = "Score: $currentScore",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}