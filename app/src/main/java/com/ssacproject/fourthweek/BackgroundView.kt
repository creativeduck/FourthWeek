package com.ssacproject.fourthweek

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.widget.Button
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class BackgroundView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int) : View(context) {

    @Volatile
    var running: Boolean = false
    var gameThread: Thread? = null

    lateinit var background : Background
    lateinit var runnable: Runnable
    lateinit var mHandler: Handler


    // 프레임 설정
    var fps: Long = 60

    var newWidth = 0
    var newHeight = 0
    val UPDATE_MILLIS = 100

    init {
        background = Background(this.context, screenWidth, screenHeight,
                        "jetpack_background", 0, 110, 400f)

        mHandler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
//                var startFrameTime: Long = System.currentTimeMillis()
//                update()
                invalidate()
//                var timeThisFrame: Long = System.currentTimeMillis() - startFrameTime
//                if (timeThisFrame >= 1) {
//                    fps = 1000 / timeThisFrame
//                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        drawBackground(canvas)
        var startFrameTime: Long = System.currentTimeMillis()
        update()
        var bg = background
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
        var timeThisFrame: Long = System.currentTimeMillis() - startFrameTime
        if (timeThisFrame >= 1) {
            fps = 1000 / timeThisFrame
        }
        mHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }
    fun update() {
        background.update(fps)
    }
    private fun drawBackground(canvas: Canvas) {
        var bg = background
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