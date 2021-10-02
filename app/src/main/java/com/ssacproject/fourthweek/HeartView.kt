package com.ssacproject.fourthweek

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.view.View
import kotlin.random.Random

class HeartView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int) : View(context) {
    var newWidth = 0
    var newHeight = 0
    var heartX = 0
//    var fireX = 0
    var random = Random.nextFloat() * 800
    val UPDATE_MILLIS = 1
    var curX = 0f
    var curY = 0f
    var heartNum = 1

    var overlapedFlag = false

    lateinit var lifeCount: Bitmap
    lateinit var heart: Bitmap
    lateinit var flame: Bitmap

    lateinit var runnable: Runnable
    lateinit var mHandler: Handler

    init {
        heart = BitmapFactory.decodeResource(resources, R.drawable.heart)
        flame = BitmapFactory.decodeResource(resources, R.drawable.flame_effect)
        newHeight = screenHeight
        newWidth = screenWidth
        heart = Bitmap.createScaledBitmap(heart, 100, 100, false)
        lifeCount = Bitmap.createScaledBitmap(heart, 65, 65, false)
        flame = Bitmap.createScaledBitmap(flame, 150, 150, false)
        mHandler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (overlapedFlag) {
            canvas.drawBitmap(flame,(heartX+newWidth-20).toFloat(), random, null)
            heartX = 0
            overlapedFlag = false
            mHandler.postDelayed(runnable, 120.toLong())
        } else {
            for (i in 0 until heartNum) {
                canvas.drawBitmap(lifeCount, (270+i*70).toFloat(), 80f, null)
            }
            heartX -= 10
            if (heartX < -newWidth-300) {
                heartX = 0
                random = Random.nextFloat() * 1000
            }
            if (heartX < screenWidth - newWidth) {
                canvas.drawBitmap(heart, (heartX+newWidth).toFloat(), random, null)
                curX = (heartX+newWidth).toFloat()
                curY = random
            }
            mHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
        }
    }
    fun overlaped() {
        overlapedFlag = true
        heartNum += 1
        mHandler.postDelayed(runnable, 70.toLong())
    }

    fun dead() {
        heartNum -= 1
        mHandler.postDelayed(runnable, 70.toLong())
    }

}