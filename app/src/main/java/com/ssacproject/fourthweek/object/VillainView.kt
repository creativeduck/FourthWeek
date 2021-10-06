package com.ssacproject.fourthweek.`object`

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Handler
import android.os.Looper
import android.view.View
import com.ssacproject.fourthweek.R
import kotlin.random.Random

class VillainView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int) : View(context) {

    companion object {
        const val UPDATE_MILLIS = 1
    }
    var SHOW_DELAY = 8

    var newWidth = 0
    var newHeight = 0
    var fireX = Random.nextInt(1, SHOW_DELAY) * 100
    var random = (Random.nextInt(1, 8) * 100).toFloat()
    var curX = 0f
    var curY = 0f
    var showRandom = 0
    var crashedFlag = false
    var fireNum = 1

    var crashedFlagList = BooleanArray(5) {false}
    var randomList = FloatArray(5) {(Random.nextInt(1, 8) * 100).toFloat()}
    var showRandomList = IntArray(5) {Random.nextInt(1, SHOW_DELAY) * 100}
    var fireXList = IntArray(5) {Random.nextInt(1, SHOW_DELAY) * 100}
    var curXList = FloatArray(5) {0f}
    var curYList = FloatArray(5) {0f}


    var flame: Bitmap
    var fire: Bitmap
    var runnable: Runnable
    var mHandler: Handler

    var onPlaying = true

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
                if (onPlaying)
                    invalidate()
            }
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until fireNum) {
            drawFire(canvas, i)
        }
    }

    fun crashed(index: Int) {
        crashedFlagList[index] = true
        mHandler.postDelayed(runnable, 70.toLong())
    }

    fun drawFire(canvas: Canvas, position: Int) {
        if (crashedFlagList[position]) {
            canvas.drawBitmap(flame, (fireXList[position]+newWidth-15).toFloat(), randomList[position], null )
            crashedFlagList[position] = false
            showRandomList[position] = (Random.nextInt(1, SHOW_DELAY) * 100)
            fireXList[position] = showRandomList[position]
            randomList[position] = (Random.nextInt(2, 8) * 100).toFloat()
            mHandler.postDelayed(runnable, 120.toLong())
        } else {
            fireXList[position] -= 10
            if (fireXList[position] < -newWidth-300) {
                showRandomList[position] = (Random.nextInt(1, SHOW_DELAY) * 100)
                fireXList[position] = showRandomList[position]
                randomList[position] = (Random.nextInt(2, 8) * 100).toFloat()
            }
            if (fireXList[position] < screenWidth - newWidth + showRandomList[position]) {
                canvas.drawBitmap(fire, (fireXList[position]+newWidth).toFloat(), randomList[position], null)
                curXList[position] = (fireXList[position]+newWidth).toFloat()
                curYList[position] = randomList[position]
            }
            mHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
        }
    }
    fun retry() {
        onPlaying = true
        mHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }

}