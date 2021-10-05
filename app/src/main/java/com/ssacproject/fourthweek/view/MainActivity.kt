package com.ssacproject.fourthweek.view

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.hardware.display.DisplayManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.room.Room
import com.ssacproject.fourthweek.R
import com.ssacproject.fourthweek.`object`.*
import com.ssacproject.fourthweek.databinding.ActivityMainBinding
import com.ssacproject.fourthweek.room.RoomBoard
import com.ssacproject.fourthweek.room.RoomBoardHelper
import com.ssacproject.fourthweek.view.LoadingActivity.Companion.boardHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : Activity() {
//    lateinit var job: Job

    val WIDTH = 2160
    val HEIGHT = 1080

    lateinit var binding: ActivityMainBinding
    lateinit var parallaxView: ParallaxView
    lateinit var userView: UserView
    lateinit var villianView: VillainView
    lateinit var backgroundView: BackgroundView
    lateinit var heartView: HeartView
    lateinit var timerView: TimerView
    lateinit var upgradeView: UpgradeView
    lateinit var targetView: TargetView
    var displayManager: DisplayManager? = null
    var mDisplayListener: DisplayManager.DisplayListener? = null
    lateinit var mediaPlayer: MediaPlayer
    var currentBGMState = 0
    var width = 0
    var height = 0
    var currentMode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        val params2 = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        binding.root.layoutParams = params2

        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        binding.container.layoutParams = params

        val windowMetrics = this.windowManager.currentWindowMetrics
        width = windowMetrics.bounds.width()
        height = windowMetrics.bounds.height()
        parallaxView = ParallaxView(this, width, height)
        binding.container.addView(parallaxView)
        // 악당 추가
        villianView = VillainView(this, width, height)
        villianView.setBackgroundColor(Color.TRANSPARENT)
        binding.container.addView(villianView)
        // 생명 아이템 추가
        heartView = HeartView(this, width, height)
        heartView.setBackgroundColor(Color.TRANSPARENT)
        binding.container.addView(heartView)
        // 타이머 추가
        timerView = TimerView(this, width, height)
        timerView.setBackgroundColor(Color.TRANSPARENT)
        binding.container.addView(timerView)
        // 업그레이드 아이템 추가
        upgradeView = UpgradeView(this, width, height)
        upgradeView.setBackgroundColor(Color.TRANSPARENT)
        binding.container.addView(upgradeView)
        // 목표 아이템 추가
        targetView = TargetView(this, width, height)
        targetView.setBackgroundColor(Color.TRANSPARENT)
        binding.container.addView(targetView)

        val character = getSharedIntData("prefCharacter", "character")
        // 주인공 추가
        userView = UserView(this, width, height, villianView, heartView, timerView,
                            upgradeView, targetView, character)
        userView.setBackgroundColor(Color.TRANSPARENT)
        binding.container.addView(userView)

        setContentView(binding.root)

        mDisplayListener = object: DisplayManager.DisplayListener {
            override fun onDisplayAdded(p0: Int) {
                Log.d("Rotation", "hi")
            }

            override fun onDisplayChanged(displayId: Int) {
                val display: Display = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                when(display.rotation) {
                    Surface.ROTATION_0 -> {
                        Log.d("Changed Rotation", "index = ${display.rotation}")
                    }
                    Surface.ROTATION_90 -> {
                        Log.d("Changed Rotation", "index = ${display.rotation}")

                    }
                    Surface.ROTATION_180 -> {
                        Log.d("Changed Rotation", "index = ${display.rotation}")

                    }
                    else -> {
                        Log.d("Changed Rotation", "index = ${display.rotation}")

                    }
                }
            }

            override fun onDisplayRemoved(p0: Int) {
            }
        }
        displayManager = this.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        displayManager!!.registerDisplayListener(mDisplayListener, Handler())

        currentMode = intent.getIntExtra("mode", 0)
        setMode(currentMode)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation

    }

    override fun onStart() {
        super.onStart()
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm_main)
        mediaPlayer.setVolume(0.5f, 0.5f)
        mediaPlayer.isLooping = true

    }

    override fun onPause() {
        super.onPause()
        parallaxView.pause()
        currentBGMState = mediaPlayer.currentPosition
        mediaPlayer.pause()

        val score = timerView.score
        setSharedData("prefScore", "score", score)
    }

    override fun onResume() {
        super.onResume()
        parallaxView.resume()
        mediaPlayer.seekTo(currentBGMState)
        mediaPlayer.start()

    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
        currentBGMState = 0
        val mode = when (currentMode) {
            0 -> "EASY"
            1 -> "NORMAL"
            else -> "HARD"
        }
        val name = getSharedStringData("prefUserName", "userName")
        val score = getSharedIntData("prefScore", "score")
        val character = getSharedIntData("prefCharacter", "character")
        CoroutineScope(IO).launch {
            boardHelper?.roomBoardDao?.insert(RoomBoard(null, name!!, character, score, mode))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (displayManager != null) {
            displayManager!!.unregisterDisplayListener(mDisplayListener)
        }
        mediaPlayer.release()
    }

    fun setSharedData(name: String, key: String, data: Int) {
        var pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = pref.edit()
        editor.putInt(key, data)
        editor.apply()
    }
    fun getSharedIntData(name: String, key: String) : Int {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        return pref.getInt(key, 0)
    }
    fun getSharedStringData(name: String, key: String) : String? {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        return pref.getString(key, "안광민")
    }
    fun setMode(mode: Int) {
        when (mode) {
            0 -> {
                heartView.SHOW_DELAY = 15
                targetView.SHOW_DELAY = 20
                upgradeView.SHOW_DELAY = 30
                villianView.SHOW_DELAY = 10
                timerView.currentTile = 1
                villianView.fireNum = 1
            }
            1 -> {
                heartView.SHOW_DELAY = 60
                targetView.SHOW_DELAY = 80
                upgradeView.SHOW_DELAY = 70
                villianView.SHOW_DELAY = 5
                timerView.currentTile = 10
                villianView.fireNum = 3
            }
            else -> {
                heartView.SHOW_DELAY = 100
                targetView.SHOW_DELAY = 140
                upgradeView.SHOW_DELAY = 140
                villianView.SHOW_DELAY = 2
                timerView.currentTile = 50
                villianView.fireNum = 5
            }
        }
    }

}