package com.example.spacefighter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.runtime.mutableStateOf

class GameView : SurfaceView, Runnable {

    var playing = false
    var gameThread : Thread? = null
    lateinit var surfaceHolder : SurfaceHolder
    lateinit var canvas : Canvas

    lateinit var paint :Paint
    var stars = arrayListOf<Star>()
    var enemies = arrayListOf<Enemy>()
    lateinit var player : Player
    lateinit var boom : Boom
    lateinit var score: Score
    lateinit var asteroid: Asteroid
    var gameOver = mutableStateOf(false)


    private fun init(context: Context, width: Int, height: Int, score: Score){
        this.score = score

        surfaceHolder = holder
        paint = Paint()

        for (i in 0..100){
            stars.add(Star(width, height))
        }

        for (i in 0..2){
            enemies.add(Enemy(context,width, height))
        }

        player = Player(context, width, height, playerSpeed = 1)
        boom = Boom(context, width, height)
        asteroid = Asteroid(context, width, height)

    }

    constructor(context: Context?, width: Int, height: Int, score: Score) : super(context) {
        init(context!!, width, height, score)
    }
    constructor(context: Context?, attrs: AttributeSet?, score: Score) : super(context, attrs){
        init(context!!, 0, 0, score)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, score: Score) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context!!, 0, 0, score)
    }

    fun resume() {
        playing = true
        gameThread = Thread(this)
        gameThread?.start()
    }

    fun pause() {
        playing = false
        gameThread?.join()
    }

    override fun run() {
        while (playing){
            update()
            draw()
            control()
        }
    }


    fun update(){

        boom.x = -300
        boom.y = -300

        for (s in stars){
            s.update(player.speed)
        }
        for (e in enemies){
            e.update(player.speed)
            if (Rect.intersects(player.detectCollision, e.detectCollision)) {
                boom.x = e.x
                boom.y = e.y

                e.x = -300

                score.addPoints(10)
            }
        }

        asteroid.update(player.speed)
        if (Rect.intersects(player.detectCollision, asteroid.detectCollision)) {
            // Handle inst-kill logic here
            playing = false
            var gameOver = mutableStateOf(false)
        }

        player.update()
    }

    fun draw(){
        if (surfaceHolder.surface.isValid){
            canvas = surfaceHolder.lockCanvas()

            // design code here

            canvas.drawColor(Color.BLACK)

            paint.color = Color.YELLOW

            for (star in stars) {
                paint.strokeWidth = star.starWidth.toFloat()
                canvas.drawPoint(star.x.toFloat(), star.y.toFloat(), paint)
            }

            canvas.drawBitmap(player.bitmap, player.x.toFloat(), player.y.toFloat(), paint)

            for (e in enemies) {
                canvas.drawBitmap(e.bitmap, e.x.toFloat(), e.y.toFloat(), paint)
            }
            canvas.drawBitmap(boom.bitmap, boom.x.toFloat(), boom.y.toFloat(), paint)


            // Draw the asteroid
            canvas.drawBitmap(asteroid.bitmap, asteroid.x.toFloat(), asteroid.y.toFloat(), paint)

            // Draw the score
            paint.color = Color.WHITE
            paint.textSize = 50f
            canvas.drawText("Score: ${score.points}", 50f, 100f, paint)

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    fun control(){
        Thread.sleep(17)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_MOVE -> {
                player.setPosition(event.y.toInt())
            }
            MotionEvent.ACTION_DOWN -> {
                player.boosting = true
            }
            MotionEvent.ACTION_UP -> {
                player.boosting = false
            }
        }
        return true
    }
}