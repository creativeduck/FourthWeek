package com.ssacproject.fourthweek

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
import com.ssacproject.fourthweek.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : Activity() {
//    lateinit var job: Job

    lateinit var binding: ActivityMainBinding
    lateinit var parallaxView: ParallaxView
    lateinit var userView: UserView
    lateinit var villianView: VillainView
    lateinit var backgroundView: BackgroundView
    lateinit var heartView: HeartView
    lateinit var timerView: TimerView
    var displayManager: DisplayManager? = null
    var mDisplayListener: DisplayManager.DisplayListener? = null
    lateinit var mediaPlayer: MediaPlayer
    var currentBGMState = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        // 오직 회전을 허용하지 않는 가로 화면만 설정할 때 사용한다. 이 경우 회전은 안 된다.
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        val params2 = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        binding.root.layoutParams = params2

        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        binding.container.layoutParams = params

        val windowMetrics = this.windowManager.currentWindowMetrics
        var width = windowMetrics.bounds.width()
        var height = windowMetrics.bounds.height()
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


        // 주인공 추가
        userView = UserView(this, width, height, villianView, heartView, timerView)
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


    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = newConfig.orientation

    }

    override fun onStart() {
        super.onStart()
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm_main)
    }

    override fun onPause() {
        super.onPause()
        parallaxView.pause()
        currentBGMState = mediaPlayer.currentPosition
        mediaPlayer.pause()
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
        editor.commit()
    }
    fun getSharedIntData(name: String, key: String) : Int {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        return pref.getInt(key, 0)
    }


}