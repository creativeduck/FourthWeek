package com.ssacproject.fourthweek

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ssacproject.fourthweek.databinding.ActivityLoadingBinding

class LoadingActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoadingBinding
    lateinit var rabbitAnimation: AnimationDrawable
    lateinit var mediaPlayer: MediaPlayer
    var currentState = 0
    var onPlay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContentView(binding.root)

        binding.rabbitLoading.apply {
            setBackgroundResource(R.drawable.rabbit_animation)
            rabbitAnimation = background as AnimationDrawable
            rabbitAnimation.start()
        }

        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnCustom.setOnClickListener {
            val intent = Intent(this, CustomActivity::class.java)
            startActivity(intent)
        }
        binding.btnSoundControl.setOnClickListener {
            if (onPlay) {
                currentState = mediaPlayer.currentPosition
                mediaPlayer.pause()
                onPlay = false
                binding.btnSoundControl.setImageResource(R.drawable.ic_mute)
            } else {
                currentState = mediaPlayer.currentPosition
                mediaPlayer.seekTo(currentState)
                mediaPlayer.start()
                onPlay = true
                binding.btnSoundControl.setImageResource(R.drawable.ic_onplay)
            }
        }
        binding.btnSetting.setOnClickListener {

        }

    }

    override fun onStart() {
        super.onStart()
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm_load)
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
        currentState = mediaPlayer.currentPosition
        onPlay = false
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.seekTo(currentState)
        mediaPlayer.start()
        onPlay = true
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
        currentState = 0
    }

    override fun onDestroy() {
        super.onDestroy()
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