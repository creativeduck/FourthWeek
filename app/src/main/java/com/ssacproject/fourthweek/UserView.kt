package com.ssacproject.fourthweek

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

class UserView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int,
    val villan: VillainView,
    val heart: HeartView,
    val timer: TimerView) : View(context) {

    val ONE_LETTER = 65

    var newWidth = 0
    var newHeight = 0
    val UPDATE_MILLIS = 70

    var jumpHeight = 0
    var isTouched = false

    var rabbitX = 0
    var rabbitY = 0
    var curX = 0f
    var curY = 0f
    var rabbitFrame = 0
    var rabbitList = arrayOfNulls<Bitmap>(3)
    lateinit var paint: Paint
    lateinit var pause : Bitmap
    lateinit var runnable: Runnable
    lateinit var rabbitHandler: Handler

    lateinit var soundPool: SoundPool
    var fireHitSoundId = 0
    var gainHeartSoundId = 0

    var playFlag = true
    var gameoverFlag = false

    init {
        rabbitList[0] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_down)
        rabbitList[0] = Bitmap.createScaledBitmap(rabbitList[0]!!, 130, 130, false)
        rabbitList[1] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_normal)
        rabbitList[1] = Bitmap.createScaledBitmap(rabbitList[1]!!, 130, 130, false)
        rabbitList[2] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_up)
        rabbitList[2] = Bitmap.createScaledBitmap(rabbitList[2]!!, 130, 130, false)
//        rabbitList[0] = getBitmapFromVector(context, R.drawable.ic_rabbit_down)
//        rabbitList[1] = getBitmapFromVector(context, R.drawable.ic_rabbit_normal)
//        rabbitList[2] = getBitmapFromVector(context, R.drawable.ic_rabbit_up)

        newHeight = screenHeight
        newWidth = screenWidth

        rabbitX = screenWidth / 2 - 800
        rabbitY = screenHeight - 350
        jumpHeight = rabbitY

        paint = Paint()

        
        pause = getBitmapFromVector(context, R.drawable.ic_baseline_pause_circle_outline_24)
        rabbitHandler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                if (playFlag)
                    invalidate()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val mAudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            soundPool = SoundPool.Builder()
                .setAudioAttributes(mAudioAttributes)
                .setMaxStreams(2).build()
        } else {
            soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)
        }

        fireHitSoundId = soundPool.load(context, R.raw.bgm_jab, 1)
        gainHeartSoundId = soundPool.load(context, R.raw.bgm_gain_heart, 1)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (playFlag) {
            canvas.drawBitmap(pause, (screenWidth-120).toFloat(), 60f, null)
            if (isTouched) {
                drawJump(canvas)
            } else {
                drawNormal(canvas)
            }
        } else if(gameoverFlag) {
            paint.setTextSize(60f);
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawText("게임오버", (screenWidth / 2-110).toFloat(), (screenHeight / 2-50).toFloat(), paint);
            canvas.drawText("다시 시작하시겠습니까?", (screenWidth / 2-300).toFloat(), (screenHeight / 2+20).toFloat(), paint);
            canvas.drawText("네", (screenWidth / 2-130).toFloat(), (screenHeight / 2+80).toFloat(), paint);
            canvas.drawText("아니요", (screenWidth / 2-45).toFloat(), (screenHeight / 2+80).toFloat(), paint);
        } else {
            paint.setTextSize(60f);
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawBitmap(rabbitList[0]!!, rabbitX.toFloat(),jumpHeight.toFloat(), null)
            canvas.drawText("일시정지", (screenWidth / 2-110).toFloat(), (screenHeight / 2-50).toFloat(), paint);
            canvas.drawText("재시작", (screenWidth / 2-170).toFloat(), (screenHeight / 2+30).toFloat(), paint);
            canvas.drawText("종료", (screenWidth / 2 + 50).toFloat(), (screenHeight / 2+30).toFloat(), paint);
            canvas.drawBitmap(pause, (screenWidth-120).toFloat(), 60f, null)
        }
        
    }

    fun drawNormal(canvas: Canvas) {
        if (jumpHeight < rabbitY) {
            jumpHeight += 23
            canvas.drawBitmap(rabbitList[0]!!, rabbitX.toFloat(),jumpHeight.toFloat(), null)
            curX = rabbitX.toFloat()
            curY = jumpHeight.toFloat()
            if (isOverlapped(curX, curY, villan.curX, villan.curY)) {
                startSound(fireHitSoundId)
                villan.crashed()
                heartChange()
            }
            if (isOverlapped(curX, curY, heart.curX, heart.curY)) {
                startSound(gainHeartSoundId)
                heart.overlaped()
            }
            rabbitHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
        } else {
            rabbitFrame++
            if (rabbitFrame == 3) {
                rabbitFrame = 0
            }
            canvas.drawBitmap(rabbitList[rabbitFrame]!!, rabbitX.toFloat(),rabbitY.toFloat(), null)
            curX = rabbitX.toFloat()
            curY = jumpHeight.toFloat()
            if (isOverlapped(curX, curY, villan.curX, villan.curY)) {
                startSound(fireHitSoundId)
                villan.crashed()
                heartChange()
            }
            if (isOverlapped(curX, curY, heart.curX, heart.curY)) {
                startSound(gainHeartSoundId)
                heart.overlaped()
            }
            rabbitHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
        }
    }

    fun drawJump(canvas: Canvas) {
        jumpHeight -= 15
        canvas.drawBitmap(rabbitList[2]!!, rabbitX.toFloat(),jumpHeight.toFloat(), null)
        curX = rabbitX.toFloat()
        curY = jumpHeight.toFloat()
        if (isOverlapped(curX, curY, villan.curX, villan.curY)) {
            soundPool.play(fireHitSoundId, 1f, 1f,
                1, 0, 1f)
            villan.crashed()
            heartChange()
        }
        if (isOverlapped(curX, curY, heart.curX, heart.curY)) {
            startSound(gainHeartSoundId)
            heart.overlaped()
        }
        rabbitHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }

    fun isOverlapped(rabbitX: Float, rabbitY: Float, targetX: Float, targetY: Float) : Boolean {
        if (rabbitX - 100 <= targetX && targetX <= rabbitX + rabbitList[0]!!.width
            && rabbitY - 100 <= targetY && targetY <= rabbitY + rabbitList[0]!!.height) {
            return true
        }
        return false
    }
    fun isOverlappedText(targetX: Float, targetY: Float, curX: Float, curY: Float,
                         letterNum: Int, lineNum: Int) : Boolean {
        if (targetX <= curX && curX <= targetX + letterNum * ONE_LETTER
            && targetY <= curY && curY <= targetY + lineNum * ONE_LETTER) {
            return true
        }

        return false
    }
    fun startSound(id: Int) {
        soundPool.play(id, 1f, 1f,
            1, 0, 1f)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.getAction() and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                isTouched = true
                var x = event.getX()
                var y = event.getY()
                if (screenWidth - 120 <= x && x <= screenWidth - 120 + pause.width
                    && 60f <= y && y <= 60f + pause.height) {
                        if (playFlag) {
                            playFlag = false
                            timer.playFlag = playFlag
                            setBackgroundColor(Color.parseColor("#66000000"))
                        } else {
                            playFlag = true
                            timer.retry()
                            timer.playFlag = playFlag
                            setBackgroundColor(Color.TRANSPARENT)
                        }
                }
                if (!playFlag) {
                    if (gameoverFlag && isOverlappedText((screenWidth / 2-130).toFloat(), (screenHeight / 2+80).toFloat(),
                            x, y, 1, 1)) {
                        restart()
                    } else if(!gameoverFlag) {
                        // 재시작
                        if (isOverlappedText((screenWidth / 2-170).toFloat(), (screenHeight / 2+30).toFloat(),
                                x, y, 3, 1)) {
                            playFlag = true
                            timer.retry()
                            timer.playFlag = playFlag
                            setBackgroundColor(Color.TRANSPARENT)
                            // 종료
                        } else if (isOverlappedText( (screenWidth / 2 + 50).toFloat(), (screenHeight / 2+30).toFloat(),
                                x, y, 2, 1)) {
                                setSharedData("prefScore", "score", timer.score)
                                val activity = context as Activity
                                activity.finish()
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                isTouched = false
            }
            else -> {}
        }
        return true
    }
    fun getBitmapFromVector(context: Context, drawableId: Int) : Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable!!)).mutate()
        }
        var bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        drawable!!.setBounds(0, 0, canvas.width, canvas.height)
        drawable!!.draw(canvas)
        return bitmap
    }

    fun heartChange() {
        if (heart.heartNum > 1) {
            heart.dead()
        } else {
            playFlag = false
            gameoverFlag = true
            Thread.sleep(50)
            timer.playFlag = playFlag
            timer.score = 0
            setBackgroundColor(Color.parseColor("#66000000"))
            rabbitHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
        }
    }
    fun restart() {
        // 타이머 재작동
        timer.score = 0
        timer.playFlag = true
        timer.retry()
        // 생명력 1로 변경
        heart.heartNum = 1
        heart.heartNum = 1
        playFlag = true
        gameoverFlag = false
        setBackgroundColor(Color.TRANSPARENT)
        rabbitHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }
    fun exit() {

    }


    fun setSharedData(name: String, key: String, data: Int) {
        val activity = context as Activity
        val pref: SharedPreferences = activity.getSharedPreferences(name, Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putInt(key, data)
        editor.apply()
    }
}