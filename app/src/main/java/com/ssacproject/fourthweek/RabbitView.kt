package com.ssacproject.fourthweek

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.media.MediaCodec.MetricsConstants.MODE
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class RabbitView(
    context: Context,
    screenWidth: Int,
    screenHeight: Int) : SurfaceView(context), Runnable {

    var rabbitThread: Thread? = null
    lateinit var ourHolder: SurfaceHolder
    @Volatile
    var playing: Boolean = false

    lateinit var canvas: Canvas
    lateinit var paint: Paint

    var fps: Long = 60
    private var timeThisFrame: Long = 0
    lateinit var bitmapRabbit: Bitmap
    var isMoving = false
    var jumpSpeedPerSecond: Float = 150f
    var rabbitXPosition: Float = 10f


    var rabbitX = 0
    var rabbitY = 0
    var rabbitFrame = 0
    var rabbitList = arrayOfNulls<Bitmap>(3)

    init {
        ourHolder = holder
        paint = Paint()
        rabbitList[0] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_down)
        rabbitList[1] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_normal)
        rabbitList[2] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_up)

        rabbitX = screenWidth / 2 - 800
        rabbitY = screenHeight - 300
    }


    override fun run() {
        while (playing) {
            var startFrameTime: Long = System.currentTimeMillis()
            update()
            Thread.sleep(170)
            draw()
            timeThisFrame = System.currentTimeMillis() - startFrameTime
            if (timeThisFrame > 0) {
                fps = 1000 / timeThisFrame
            }
        }
    }

    fun update() {
        rabbitFrame++
        if (rabbitFrame == 3) {
            rabbitFrame = 0
        }
    }

    fun draw() {
        if (ourHolder.surface.isValid) {
            canvas = ourHolder.lockCanvas()
            // 이전에 사용했던 이미지 제거
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            canvas.drawBitmap(rabbitList[rabbitFrame]!!, rabbitX.toFloat(),rabbitY.toFloat(), null)
            ourHolder.unlockCanvasAndPost(canvas)
        }
    }
    fun pause() {
        playing = false
        try {
            rabbitThread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
    fun resume() {
        playing = true
        rabbitThread = Thread(this)
        rabbitThread!!.start()
    }

//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        when (event.getAction() and MotionEvent.ACTION_MASK) {
//            MotionEvent.ACTION_DOWN ->
//                // Set isMoving so Bob is moved in the update method
//                isMoving = true
//            MotionEvent.ACTION_UP ->
//                // Set isMoving so Bob does not move
//                isMoving = false
//            else -> {}
//        }
//        return true
//    }

}