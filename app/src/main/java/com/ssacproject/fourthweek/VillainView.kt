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

class VillainView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int) : View(context) {
    var newWidth = 0
    var newHeight = 0
    var fireX = 0
    var random = Random.nextFloat() * 800
    val UPDATE_MILLIS = 1
    var curX = 0f
    var curY = 0f

    var crashedFlag = false

    lateinit var flame: Bitmap
    lateinit var fire: Bitmap
    lateinit var runnable: Runnable
    lateinit var mHandler: Handler

    init {
        fire = BitmapFactory.decodeResource(resources, R.drawable.fire)
        flame = BitmapFactory.decodeResource(resources, R.drawable.flame_effect)
        newHeight = screenHeight
        newWidth = screenWidth
        fire = Bitmap.createScaledBitmap(fire, 100, 100, false)
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
        if (crashedFlag) {
            canvas.drawBitmap(flame, (fireX+newWidth-15).toFloat(), random, null )
            crashedFlag = false
            fireX = 0
            random = Random.nextFloat() * 1000
            mHandler.postDelayed(runnable, 120.toLong())
        }
        fireX -= 10
        if (fireX < -newWidth-300) {
            fireX = 0
            random = Random.nextFloat() * 1000
        }
        if (fireX < screenWidth - newWidth) {
            canvas.drawBitmap(fire, (fireX+newWidth).toFloat(), random, null)
            curX = (fireX+newWidth).toFloat()
            curY = random
        }
        mHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }

    fun crashed() {
        crashedFlag = true
        mHandler.postDelayed(runnable, 70.toLong())
    }

}