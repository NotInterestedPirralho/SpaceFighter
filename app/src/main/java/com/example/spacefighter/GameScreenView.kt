package com.example.spacefighter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import android.content.Context
import androidx.compose.ui.platform.LocalContext

@Composable
fun GameScreenView (score: Score, onScoreChange: () -> Unit) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val context = LocalContext.current
    val player = remember { Player(context, screenWidth, screenHeight,  playerSpeed = 1) }
    val enemies = remember { mutableListOf<Enemy>() }

    val density = configuration.densityDpi / 160f
    val screenWidthPx = screenWidth * density
    val screenHeightPx = screenHeight * density

    LaunchedEffect(Unit) {
        while (true) {
            // Update player and enemies
            player.update()
            enemies.forEach { it.update(player.speed) }

            // Check for collisions
            enemies.forEach { enemy ->
                if (player.checkCollision(enemy)) {
                    score.addPoints(10)
                    onScoreChange()
                }
            }

            // Delay to control the game loop speed
            delay(16L)
        }
    }

    AndroidView(factory = { context ->
        GameView(context = context,
            width = screenWidthPx.toInt(),
            height = screenHeightPx.toInt(),
            score = score)
    },
        update = {
            it.resume()
        }
    )
}