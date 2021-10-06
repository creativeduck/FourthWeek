package com.ssacproject.fourthweek.`object`

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.view.View
import com.ssacproject.fourthweek.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

class HeartView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int) : View(context) {

    companion object {
        const val UPDATE_MILLIS = 1
    }
    var SHOW_DELAY = 15
    var newWidth = 0
    var newHeight = 0
    var heartX = 0
    var random = (Random.nextInt(1, 8) * 100).toFloat()
    var curX = 0f
    var curY = 0f
    var heartNum = 1
    var showRandom = (Random.nextInt(1, 8) * 100)

    var overlapedFlag = false

    var lifeCount: Bitmap
    var heart: Bitmap
    var flame: Bitmap
    var runnable: Runnable
    var mHandler: Handler

    var onPlaying = true

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
                if (onPlaying)
                    invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (overlapedFlag) {
            canvas.drawBitmap(flame,(heartX+newWidth-20).toFloat(), random, null)
            showRandom = (Random.nextInt(1, SHOW_DELAY) * 100)
            heartX = showRandom
            overlapedFlag = false
            // 이부분 변경
            mHandler.postDelayed(runnable, 120.toLong())
        } else {
            for (i in 0 until heartNum) {
                canvas.drawBitmap(lifeCount, (40+i*70).toFloat(), 140f, null)
            }
            heartX -= 10
            if (heartX < -newWidth-300) {
                showRandom = (Random.nextInt(1, SHOW_DELAY) * 100)
                heartX = showRandom
                random = (Random.nextInt(1, 8) * 100).toFloat()
            }
            if (heartX < screenWidth - newWidth + showRandom) {
                canvas.drawBitmap(heart, (heartX+newWidth).toFloat(), random, null)
                curX = (heartX+newWidth).toFloat()
                curY = random
            }
            mHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
        }
    }

    fun overlaped(flag: Boolean) {
        synchronized(this) {
            overlapedFlag = flag
            if (flag) {
                heartNum += 1
                mHandler.postDelayed(runnable, 70.toLong())
            }
            else {
                heartNum -= 1
                mHandler.postDelayed(runnable, 70.toLong())
            }
        }
    }

    fun dead() {
        heartNum -= 1
        mHandler.postDelayed(runnable, 70.toLong())
    }
    fun retry() {
        onPlaying = true
        mHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }
}