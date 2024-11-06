package com.example.spacefighter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

class Asteroid(context: Context, width: Int, height: Int) {

    var x = 0
    var y = 0
    var speed = 0
    var maxX = 0
    var maxY = 0
    var minX = 0
    var minY = 0

    var bitmap: Bitmap
    var detectCollision: Rect

    init {
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.asteroid).let { Bitmap.createScaledBitmap(it, 100, 100, false) }

        minX = 0
        maxX = width
        maxY = height - bitmap.height
        minY = 0

        x = width
        y = (0..maxY).random()

        speed = 10

        detectCollision = Rect(x, y, bitmap.width, bitmap.height)
    }

    fun update(playerSpeed: Int) {
        x -= speed + playerSpeed
        if (x < minX) {
            x = maxX
            y = (0..maxY).random()
        }

        detectCollision.left = x
        detectCollision.top = y
        detectCollision.right = x + bitmap.width
        detectCollision.bottom = y + bitmap.height
    }
}