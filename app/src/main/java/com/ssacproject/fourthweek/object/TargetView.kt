package com.ssacproject.fourthweek.`object`

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.view.View
import com.ssacproject.fourthweek.R
import kotlin.random.Random

class TargetView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int) : View(context) {

    companion object {
        const val UPDATE_MILLIS = 1
    }
    var SHOW_DELAY = 20
    var newWidth = 0
    var newHeight = 0
    var targetX = Random.nextInt(1, SHOW_DELAY) * 100
    var random = Random.nextFloat() * 800

    var currentTitle = ""
    var curX = 0f
    var curY = 0f
    var showRandom = 0
    var howmuch = 0
    var randomIndex = 0

    var crashedFlag = false

    var paint: Paint
    var flame: Bitmap
    var targetK: Bitmap
    var targetO: Bitmap
    var targetT: Bitmap
    var targetL: Bitmap
    var targetI: Bitmap
    var targetN: Bitmap
    var runnable: Runnable
    var mHandler: Handler

    var letters = listOf("K", "O", "T", "L", "I", "N")

    var yesList = BooleanArray(6) {false}

    var targetImageList = arrayOf(R.drawable.kotlin_k, R.drawable.kotlin_o, R.drawable.kotlin_t,
        R.drawable.kotlin_l, R.drawable.kotlin_i, R.drawable.kotlin_n)

    init {
//        var targetList = arrayOf(targetK, targetO, targetT, targetL, targetI, targetN)
//        for (i in 0 until targetList.size) run {
//            targetList[i] = BitmapFactory.decodeResource(resources, targetImageList[i])
//            targetList[i] = Bitmap.createScaledBitmap(targetList[i], 100, 100, false)
//        }
        targetK = BitmapFactory.decodeResource(resources, R.drawable.kotlin_k)
        targetK = Bitmap.createScaledBitmap(targetK, 100, 100, false)
        targetO = BitmapFactory.decodeResource(resources, R.drawable.kotlin_o)
        targetO = Bitmap.createScaledBitmap(targetO, 100, 100, false)
        targetT = BitmapFactory.decodeResource(resources, R.drawable.kotlin_t)
        targetT = Bitmap.createScaledBitmap(targetT, 100, 100, false)
        targetL = BitmapFactory.decodeResource(resources, R.drawable.kotlin_l)
        targetL = Bitmap.createScaledBitmap(targetL, 100, 100, false)
        targetI = BitmapFactory.decodeResource(resources, R.drawable.kotlin_i)
        targetI = Bitmap.createScaledBitmap(targetI, 100, 100, false)
        targetN = BitmapFactory.decodeResource(resources, R.drawable.kotlin_n)
        targetN = Bitmap.createScaledBitmap(targetN, 100, 100, false)

        paint = Paint()
        paint.setColor(Color.WHITE)
        paint.textSize = 60f
        flame = BitmapFactory.decodeResource(resources, R.drawable.flame_effect)
        newHeight = screenHeight
        newWidth = screenWidth
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
        drawTargetss(canvas)
        if (crashedFlag) {
            canvas.drawBitmap(flame, (targetX+newWidth-15).toFloat(), random, null )
            crashedFlag = false
            showRandom = (Random.nextInt(1, SHOW_DELAY) * 100)
            randomIndex = Random.nextInt(0, 6)
            targetX = showRandom
            random = Random.nextFloat() * 1000
            mHandler.postDelayed(runnable, 120.toLong())
        }
        targetX -= 10
        if (targetX < -newWidth-300) {
            showRandom = (Random.nextInt(1, SHOW_DELAY) * 100)
            targetX = showRandom
            random = Random.nextFloat() * 1000
            randomIndex = Random.nextInt(0, 6)
        }
        if (targetX < screenWidth - newWidth + showRandom) {
            if (random < 100f) {
                howmuch = +10
            } else if (random > screenHeight - 300) {
                howmuch = -10
            }
            random += howmuch
            when (randomIndex) {
                0 -> {
                    canvas.drawBitmap(targetK, (targetX+newWidth).toFloat(), random, null)
                    currentTitle = "K"
                }
                1 -> {
                    canvas.drawBitmap(targetO, (targetX+newWidth).toFloat(), random, null)
                    currentTitle = "O"
                }
                2 -> {
                    canvas.drawBitmap(targetT, (targetX+newWidth).toFloat(), random, null)
                    currentTitle = "T"
                }
                3 -> {
                    canvas.drawBitmap(targetL, (targetX+newWidth).toFloat(), random, null)
                    currentTitle = "L"
                }
                4 -> {
                    canvas.drawBitmap(targetI, (targetX+newWidth).toFloat(), random, null)
                    currentTitle = "I"
                }
                else -> {
                    canvas.drawBitmap(targetN, (targetX+newWidth).toFloat(), random, null)
                    currentTitle = "N"
                }
            }
            curX = (targetX+newWidth).toFloat()
            curY = random
        }
        mHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }

    fun crashed() {
        crashedFlag = true
        mHandler.postDelayed(runnable, 70.toLong())
    }
    fun plus(index: Int) {
        yesList[index] = true
    }

    fun drawTargetss(canvas: Canvas) {
        for (i in 0 until letters.size) {
            val name =  letters[i]
            if (yesList[i]) {
                paint.setColor(Color.WHITE)
            } else {
                paint.setColor(Color.parseColor("#CCD8D8D8"))
            }
            if (i == 5) {
                canvas.drawText(name, (screenWidth-120-80 - (6-i)* 40-20).toFloat(), 130f, paint)
            } else {
                canvas.drawText(name, (screenWidth-120-80 - (6-i)* 40).toFloat(), 130f, paint)
            }
        }
    }

}