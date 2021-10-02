package com.ssacproject.fourthweek

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.view.View
import kotlin.concurrent.timerTask
import kotlin.random.Random

class TimerView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int) : View(context) {
    var newWidth = 0
    var newHeight = 0

    lateinit var paint: Paint
    var score = 0
    var currentTile = 2
    lateinit var mHandler: Handler
    var playFlag = true
    lateinit var timetask: Runnable

    init {
        mHandler = Handler(Looper.getMainLooper())
        timetask = object : Runnable {
            override fun run() {
                if (playFlag) {
                    score += currentTile
                    invalidate()
                    mHandler.postDelayed(this, 500)
                }
            }
        }
        mHandler.removeCallbacks(timetask)
        mHandler.postDelayed(timetask, 500)
        paint = Paint()
        paint.setTextSize(60f);
        paint.setColor(Color.argb(255, 255, 255, 255));
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText("${score}점", 40f, 130f, paint);
    }
    fun retry() {
        mHandler.postDelayed(timetask, 500)
    }

}