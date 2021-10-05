package com.ssacproject.fourthweek.`object`

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
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.ssacproject.fourthweek.R
import com.ssacproject.fourthweek.view.LoadingActivity

class UserView(
    context: Context,
    val screenWidth: Int,
    val screenHeight: Int,
    val villan: VillainView,
    val heart: HeartView,
    val timer: TimerView,
    val upgrade: UpgradeView,
    val target: TargetView,
    val character: Int
) : View(context) {

    companion object {
        const val ONE_LETTER = 65
        const val UPDATE_MILLIS = 70
    }

    var newWidth = 0
    var newHeight = 0
    var jumpHeight = 0
    var isTouched = false
    var characterX = 0
    var characterY = 0
    var curX = 0f
    var curY = 0f

    val targetIndexList = listOf("K", "O", "T", "L", "I", "N")
    // 토끼
    var rabbitFrame = 0
    var rabbitFrameMax = 3
    var rabbitList = arrayOfNulls<Bitmap>(3)
    // 새
    var birdFrame = 0
    var birdFrameMax = 9
    var birdList = arrayOfNulls<Bitmap>(9)
    // 돼지
    var pigFrame = 0
    var pigFrameMax = 5
    var pigList = arrayOfNulls<Bitmap>(5)
    // 유령
    var ghostFrame = 0
    var ghostFrameMax = 6
    var ghostList = arrayOfNulls<Bitmap>(6)

    var characterFrame = 0
    var characterFrameMax = 0

    var characterList : Array<Bitmap?>

    var fireHitSoundId = 0
    var gainHeartSoundId = 0
    var upgradeSoundId = 0
    var targetSoundId = 0
    val targetSet = HashSet<String>()
    // 플래그들
    var winFlag = false
    var playFlag = true
    var gameoverFlag = false

    var paint: Paint
    var pause : Bitmap
    var runnable: Runnable
    var rabbitHandler: Handler
    var soundPool: SoundPool

    init {
        rabbitList[0] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_down)
        rabbitList[0] = Bitmap.createScaledBitmap(rabbitList[0]!!, 130, 130, false)
        rabbitList[1] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_normal)
        rabbitList[1] = Bitmap.createScaledBitmap(rabbitList[1]!!, 130, 130, false)
        rabbitList[2] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_up)
        rabbitList[2] = Bitmap.createScaledBitmap(rabbitList[2]!!, 130, 130, false)

        birdList[0] = BitmapFactory.decodeResource(resources, R.drawable.fly1)
        birdList[0] = Bitmap.createScaledBitmap(birdList[0]!!, 130, 130, false)
        birdList[1] = BitmapFactory.decodeResource(resources, R.drawable.fly2)
        birdList[1] = Bitmap.createScaledBitmap(birdList[1]!!, 130, 130, false)
        birdList[2] = BitmapFactory.decodeResource(resources, R.drawable.fly3)
        birdList[2] = Bitmap.createScaledBitmap(birdList[2]!!, 130, 130, false)
        birdList[3] = BitmapFactory.decodeResource(resources, R.drawable.fly4)
        birdList[3] = Bitmap.createScaledBitmap(birdList[3]!!, 130, 130, false)
        birdList[4] = BitmapFactory.decodeResource(resources, R.drawable.fly5)
        birdList[4] = Bitmap.createScaledBitmap(birdList[4]!!, 130, 130, false)
        birdList[5] = BitmapFactory.decodeResource(resources, R.drawable.fly6)
        birdList[5] = Bitmap.createScaledBitmap(birdList[5]!!, 130, 130, false)
        birdList[6] = BitmapFactory.decodeResource(resources, R.drawable.fly7)
        birdList[6] = Bitmap.createScaledBitmap(birdList[6]!!, 130, 130, false)
        birdList[7] = BitmapFactory.decodeResource(resources, R.drawable.fly8)
        birdList[7] = Bitmap.createScaledBitmap(birdList[7]!!, 130, 130, false)
        birdList[8] = BitmapFactory.decodeResource(resources, R.drawable.fly9)
        birdList[8] = Bitmap.createScaledBitmap(birdList[8]!!, 130, 130, false)

        pigList[0] = BitmapFactory.decodeResource(resources, R.drawable.pig1)
        pigList[0] = Bitmap.createScaledBitmap(pigList[0]!!, 130, 130, false)
        pigList[1] = BitmapFactory.decodeResource(resources, R.drawable.pig2)
        pigList[1] = Bitmap.createScaledBitmap(pigList[1]!!, 130, 130, false)
        pigList[2] = BitmapFactory.decodeResource(resources, R.drawable.pig3)
        pigList[2] = Bitmap.createScaledBitmap(pigList[2]!!, 130, 130, false)
        pigList[3] = BitmapFactory.decodeResource(resources, R.drawable.pig4)
        pigList[3] = Bitmap.createScaledBitmap(pigList[3]!!, 130, 130, false)
        pigList[4] = BitmapFactory.decodeResource(resources, R.drawable.pig5)
        pigList[4] = Bitmap.createScaledBitmap(pigList[4]!!, 130, 130, false)

        ghostList[0] = BitmapFactory.decodeResource(resources, R.drawable.ghost1)
        ghostList[0] = Bitmap.createScaledBitmap(ghostList[0]!!, 130, 130, false)
        ghostList[1] = BitmapFactory.decodeResource(resources, R.drawable.ghost2)
        ghostList[1] = Bitmap.createScaledBitmap(ghostList[1]!!, 130, 130, false)
        ghostList[2] = BitmapFactory.decodeResource(resources, R.drawable.ghost3)
        ghostList[2] = Bitmap.createScaledBitmap(ghostList[2]!!, 130, 130, false)
        ghostList[3] = BitmapFactory.decodeResource(resources, R.drawable.ghost4)
        ghostList[3] = Bitmap.createScaledBitmap(ghostList[3]!!, 130, 130, false)
        ghostList[4] = BitmapFactory.decodeResource(resources, R.drawable.ghost5)
        ghostList[4] = Bitmap.createScaledBitmap(ghostList[4]!!, 130, 130, false)
        ghostList[5] = BitmapFactory.decodeResource(resources, R.drawable.ghost6)
        ghostList[5] = Bitmap.createScaledBitmap(ghostList[5]!!, 130, 130, false)



        when (character) {
            LoadingActivity.CHARACTER_BIRD -> {
                characterFrame = birdFrame
                characterList = birdList
                characterFrameMax = birdFrameMax
            }
            LoadingActivity.CHARACTER_PIG -> {
                characterFrame = pigFrame
                characterList = pigList
                characterFrameMax = pigFrameMax
            }
            LoadingActivity.CHARACTER_GHOST -> {
                characterFrame = ghostFrame
                characterList = ghostList
                characterFrameMax = ghostFrameMax
            }
            else -> {
                characterFrame = rabbitFrame
                characterFrameMax = rabbitFrameMax
                characterList = rabbitList
            }
        }

        newHeight = screenHeight
        newWidth = screenWidth

        characterX = screenWidth / 2 - 800
        characterY = screenHeight - 350
        jumpHeight = characterY

        paint = Paint()
        paint.setTextSize(60f);
        paint.setColor(Color.argb(255, 255, 255, 255));

        
        pause = getBitmapFromVector(context, R.drawable.ic_baseline_pause_circle_outline_24)
        rabbitHandler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                if (playFlag) {
                    invalidate()
                }

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
        upgradeSoundId = soundPool.load(context, R.raw.bgm_tiring, 1)
        targetSoundId = soundPool.load(context, R.raw.bgm_target, 1)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (winFlag) {
            paint.textSize = 120f
            canvas.drawText("게임에 승리하셨습니다", (screenWidth / 2-530).toFloat(), (screenHeight / 2-50).toFloat(), paint);
            paint.textSize = 60f
            canvas.drawText("다시 시작하시겠습니까?", (screenWidth / 2-300).toFloat(), (screenHeight / 2+40).toFloat(), paint);
            canvas.drawText("네", (screenWidth / 2-130).toFloat(), (screenHeight / 2+115).toFloat(), paint);
            canvas.drawText("아니요", (screenWidth / 2-45).toFloat(), (screenHeight / 2+115).toFloat(), paint);
        } else {
            if (playFlag) {
                canvas.drawBitmap(pause, (screenWidth-120).toFloat(), 60f, null)
                if (isTouched) {
                    drawJump(canvas)
                } else {
                    drawNormal(canvas)
                }
            } else if(gameoverFlag) {
                canvas.drawText("게임오버", (screenWidth / 2-110).toFloat(), (screenHeight / 2-50).toFloat(), paint);
                canvas.drawText("다시 시작하시겠습니까?", (screenWidth / 2-300).toFloat(), (screenHeight / 2+20).toFloat(), paint);
                canvas.drawText("네", (screenWidth / 2-130).toFloat(), (screenHeight / 2+90).toFloat(), paint);
                canvas.drawText("아니요", (screenWidth / 2-45).toFloat(), (screenHeight / 2+90).toFloat(), paint);
            } else {
                canvas.drawBitmap(characterList[characterFrame]!!, characterX.toFloat(),jumpHeight.toFloat(), null)
                canvas.drawText("일시정지", (screenWidth / 2-110).toFloat(), (screenHeight / 2-50).toFloat(), paint);
                canvas.drawText("재시작", (screenWidth / 2-170).toFloat(), (screenHeight / 2+30).toFloat(), paint);
                canvas.drawText("종료", (screenWidth / 2 + 50).toFloat(), (screenHeight / 2+30).toFloat(), paint);
                canvas.drawBitmap(pause, (screenWidth-120).toFloat(), 60f, null)
            }
        }
    }

    fun drawNormal(canvas: Canvas) {
        characterFrame++
        if (characterFrame == characterFrameMax) {
            characterFrame = 0
        }
        if (jumpHeight < characterY) {
            jumpHeight += 23
            canvas.drawBitmap(characterList[0]!!, characterX.toFloat(),jumpHeight.toFloat(), null)
            curX = characterX.toFloat()
            curY = jumpHeight.toFloat()
            whenCrashed()
            rabbitHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
        } else {
            canvas.drawBitmap(characterList[characterFrame]!!, characterX.toFloat(),characterY.toFloat(), null)
            curX = characterX.toFloat()
            curY = jumpHeight.toFloat()
            whenCrashed()
            rabbitHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
        }
    }

    fun drawJump(canvas: Canvas) {
        jumpHeight -= 15
        characterFrame++
        if (characterFrame == characterFrameMax) {
            characterFrame = 0
        }
        canvas.drawBitmap(characterList[characterFrame]!!, characterX.toFloat(),jumpHeight.toFloat(), null)
        curX = characterX.toFloat()
        curY = jumpHeight.toFloat()
        whenCrashed()
        rabbitHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }

    fun isOverlapped(X: Float, Y: Float, targetX: Float, targetY: Float) : Boolean {
        if (X - 100 <= targetX && targetX <= X + characterList[0]!!.width
            && Y - 100 <= targetY && targetY <= Y + characterList[0]!!.height) {
            return true
        }
        return false
    }
    fun isOverlappedText(targetX: Float, targetY: Float, curX: Float, curY: Float,
                         letterNum: Int, lineNum: Int) : Boolean {
        if (targetX <= curX && curX <= targetX + letterNum * ONE_LETTER
            && targetY - lineNum * ONE_LETTER <= curY && curY <= targetY) {
            return true
        }
        return false
    }
    fun startSound(id: Int) {
        soundPool.play(id, 1f, 1f,
            1, 0, 1f)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.getX()
        val y = event.getY()
        when (event.getAction() and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val score = timer.score

                isTouched = true
                pauseTouched(x, y)


                Log.d("TIMEVIEW1", "${score}")

                if (winFlag) {
                    // 게임승리
                    setSharedData("prefScore", "score", score)
                    if (isOverlappedText((screenWidth / 2-130).toFloat(), (screenHeight / 2+115).toFloat(),
                            x, y, 1, 1)) {
                        // 게임 다시 시작
                        restart()
                        }
                    else if (isOverlappedText((screenWidth / 2-45).toFloat(), (screenHeight / 2+115).toFloat(),
                                x, y, 3, 1)) {
                        // 게임 종료
                        val activity = context as Activity
                        activity.finish()
                    }
                }
                // 게임 멈추었을 때
                else if (!playFlag) {

                    Log.d("TIMEVIEW7", "${score}")

                    // 게임오버되었을 때 물어봄
                    if (gameoverFlag) {

                        Log.d("TIMEVIEW8", "${score}")

                        if (isOverlappedText((screenWidth / 2-45).toFloat(), (screenHeight / 2+90).toFloat(),
                                x, y, 3, 1)) {

                            Log.d("TIMEVIEW99", "${score}")

                            setSharedData("prefScore", "score",score)

                            val activity = context as Activity
                            activity.finish()
                        } else if (isOverlappedText((screenWidth / 2-130).toFloat(), (screenHeight / 2+90).toFloat(),
                                x, y, 1, 1)) {
                            restart()
                        }
                    }
                    // 게임오버는 아니고 일시정지할 때
                    else if (!gameoverFlag) {
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
                            setSharedData("prefScore", "score", score)
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
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable!!.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun heartChange() {
        if (heart.heartNum > 1) {
            heart.dead()
        } else {
            playFlag = false
            gameoverFlag = true
            timer.playFlag = playFlag
//            timer.score = 0
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
        winFlag = false
        setBackgroundColor(Color.TRANSPARENT)
        rabbitHandler.postDelayed(runnable, UPDATE_MILLIS.toLong())
    }

    fun setSharedData(name: String, key: String, data: Int) {
        val activity = context as Activity
        val pref: SharedPreferences = activity.getSharedPreferences(name, Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putInt(key, data)
        editor.apply()
    }
    fun whenCrashed() {
        // 악당과 부딪혔을 때
        for (i in 0 until villan.fireNum) {
            if (isOverlapped(curX, curY, villan.curXList[i], villan.curYList[i])) {
                startSound(fireHitSoundId)
                villan.crashed(i)
                heartChange()
            }
        }
        // 하트를 먹었을 때
        if (isOverlapped(curX, curY, heart.curX, heart.curY)) {
            startSound(gainHeartSoundId)
            heart.overlaped()
        }
        // 업그레이드 아이템 먹었을 때
        if (isOverlapped(curX, curY, upgrade.curX, upgrade.curY)) {
            upgrade.crashed()
            startSound(upgradeSoundId)
            timer.score *= 2
        }
        // 목표 아이템 먹었을 때
        if (isOverlapped(curX, curY, target.curX, target.curY)) {
            target.crashed()
            startSound(targetSoundId)
            val name = target.currentTitle
            if (targetSet.add(name)) {
                val index = targetIndexList.indexOf(name)
                target.plus(index)
            }
            if (targetSet.size == 6) {
                playFlag = false
                gameoverFlag = false
                winFlag = true
                timer.playFlag = playFlag
                setBackgroundColor(Color.parseColor("#66000000"))
            }
        }
    }
    fun pauseTouched(x: Float, y: Float) {
        if (screenWidth - 120 <= x && x <= screenWidth - 120 + pause.width
            && 60f <= y && y <= 60f + pause.height
        ) {
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
    }
}