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

class UpgradeView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int) : View(context) {

    companion object {
        const val UPDATE_MILLIS = 1
    }
    var SHOW_DELAY = 32

    var newWidth = 0
    var newHeight = 0
    var upgradeX = Random.nextInt(1, SHOW_DELAY) * 100
    var random = Random.nextFloat() * 800
    val UPDATE_MILLIS = 1

    var curX = 0f
    var curY = 0f
    var showRandom = 0

    var crashedFlag = false
    var howmuch = 10

    var flame: Bitmap
    var upgrade: Bitmap
    var runnable: Runnable
    var mHandler: Handler

    var onPlaying = true

    init {
        upgrade = BitmapFactory.decodeResource(resources, R.drawable.item_upgrade)
        flame = BitmapFactory.decodeResource(resources, R.drawable.flame_effect)
        newHeight = screenHeight
        newWidth = screenWidth
        upgrade = Bitmap.createScaledBitmap(upgrade, 100, 100, false)
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
        if (crashedFlag) {
            canvas.drawBitmap(flame, (upgradeX+newWidth-15).toFloat(), random, null )
            crashedFlag = false
            showRandom = (Random.nextInt(1, SHOW_DELAY) * 100)
            upgradeX = showRandom
            random = Random.nextFloat() * 1000
            mHandler.postDelayed(runnable, 120.toLong())
        }
        upgradeX -= 10
        if (upgradeX < -newWidth-300) {
            showRandom = (Random.nextInt(1, SHOW_DELAY) * 100)
            upgradeX = showRandom
            random = Random.nextFloat() * 1000
        }
        if (upgradeX < screenWidth - newWidth + showRandom) {
            if (random < 100f) {
                howmuch = +10
            } else if (random > screenHeight - 300) {
                howmuch = -10
            }
            random += howmuch
            canvas.drawBitmap(upgrade, (upgradeX+newWidth).toFloat(), random, null)
            curX = (upgradeX+newWidth).toFloat()
            curY = random
        }
        mHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }

    fun crashed() {
        crashedFlag = true
        mHandler.postDelayed(runnable, 70.toLong())
    }
    fun retry() {
        onPlaying = true
        mHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }

}