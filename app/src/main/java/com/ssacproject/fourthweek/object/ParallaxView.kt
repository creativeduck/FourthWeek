package com.ssacproject.fourthweek.`object`

import android.content.Context
import android.graphics.*
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.ssacproject.fourthweek.Background

class ParallaxView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int
) : SurfaceView(context), Runnable{
    @Volatile
    var running: Boolean = false
    var gameThread: Thread? = null
    lateinit var backgrounds : ArrayList<Background>

    lateinit var paint: Paint
    lateinit var canvas: Canvas
    lateinit var ourHolder: SurfaceHolder
    // 프레임 설정
    var fps: Long = 60

    init {
        ourHolder = holder
        paint = Paint()
        backgrounds = ArrayList<Background>()

        backgrounds.add(
            Background(
            this.context,
            screenWidth,
            screenHeight,
            "jetpack_background", 0, 110, 400f)
        )

    }

    override fun run() {
        while(running) {
            var startFrameTime: Long = System.currentTimeMillis()
            update()
            draw()
            var timeThisFrame: Long = System.currentTimeMillis() - startFrameTime
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame
            }
        }
    }
    fun update() {
        for (bg in backgrounds) {
            bg.update(fps)
        }
    }
    fun draw() {
        if (ourHolder.surface.isValid) {
            canvas = ourHolder.lockCanvas()
            drawBackground(0)
            ourHolder.unlockCanvasAndPost(canvas)
        }
    }
    fun pause() {
        running = false
        try {
            gameThread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
    fun resume() {
        running = true
        gameThread = Thread(this)
        gameThread!!.start()
    }

    private fun drawBackground(position: Int) {
        var bg: Background = backgrounds[position]

        var fromRect1 = Rect(0, 0, bg.width - bg.xClip, bg.height)
        var toRect1 = Rect(bg.xClip, bg.startY, bg.width, bg.endY)

        var fromRect2 = Rect(bg.width - bg.xClip, 0, bg.width, bg.height)
        var toRect2 = Rect(0, bg.startY, bg.xClip, bg.endY)

        if (!bg.reversedFirst) {
            canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, null)
            canvas.drawBitmap(bg.bitmapReserved, fromRect2, toRect2, null)
        } else {
            canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, null)
            canvas.drawBitmap(bg.bitmapReserved, fromRect1, toRect1, null)
        }
    }
}