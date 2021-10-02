package com.ssacproject.fourthweek

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix

class Background(
    context: Context,
    screenWidth: Int,
    screenHeight: Int,
    bitmapName: String,
    sY: Int,
    eY: Int,
    s: Float) {
    lateinit var bitmap: Bitmap
    lateinit var bitmapReserved: Bitmap
    var width: Int = 0
    var height: Int = 0
    var reversedFirst: Boolean = false
    var speed: Float = 0f
    var xClip: Int = 0
    var startY: Int = 0
    var endY: Int = 0

    init {
        val resId: Int = context.resources.getIdentifier(bitmapName,
                            "drawable", context.packageName)
        bitmap = BitmapFactory.decodeResource(context.resources, resId)
        reversedFirst = false

        xClip = 0

        startY = sY * (screenHeight / 100)
        endY = eY * (screenHeight / 100)
        speed = s

        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth,
            (endY - startY), true)
        width = bitmap.width
        height = bitmap.height

        val matrix = Matrix()
        matrix.setScale(-1f, 1f)
        bitmapReserved = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)

    }

    fun update(fps: Long) {
        xClip -= (speed/fps).toInt()
//        xClip -= 7
        if (xClip >= width) {
            xClip = 0
            reversedFirst = !reversedFirst
        } else if (xClip <= 0) {
            xClip = width
            reversedFirst = !reversedFirst
        }
    }

}