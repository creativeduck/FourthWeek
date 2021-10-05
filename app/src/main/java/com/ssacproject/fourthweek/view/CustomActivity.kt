package com.ssacproject.fourthweek.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ssacproject.fourthweek.R
import com.ssacproject.fourthweek.databinding.ActivityCustomBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomActivity : AppCompatActivity() {
    lateinit var binding: ActivityCustomBinding

    var btnClickSoundId = 0
    lateinit var soundPool: SoundPool


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomBinding.inflate(layoutInflater)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContentView(binding.root)

        val curCharacter = getSharedIntData("prefCharacter", "character")
        when (curCharacter) {
            LoadingActivity.CHARACTER_BIRD -> {
                binding.linearBird.setBackgroundResource(R.drawable.mode_linear_shape_selected)
            }
            LoadingActivity.CHARACTER_PIG -> {
                binding.linearPig.setBackgroundResource(R.drawable.mode_linear_shape_selected)
            }
            LoadingActivity.CHARACTER_GHOST -> {
                binding.linearGhost.setBackgroundResource(R.drawable.mode_linear_shape_selected)
            }
            else -> {
                binding.linearRabbit.setBackgroundResource(R.drawable.mode_linear_shape_selected)
            }
        }
        binding.choiceRabbit.apply {
            setBackgroundResource(R.drawable.animation_rabbit)
            val animation = background as AnimationDrawable
            animation.start()
        }
        binding.linearRabbit.apply {
            setOnClickListener {
                if (btnClickSoundId != 0)
                    startSound(btnClickSoundId)
                setSharedData("prefCharacter", "character", LoadingActivity.CHARACTER_RABBIT)
                resetColor()
                binding.linearRabbit.setBackgroundResource(R.drawable.mode_linear_shape_selected)
            }
        }
        binding.choiceBird.apply {
            setBackgroundResource(R.drawable.animation_bird)
            val animation = background as AnimationDrawable
            animation.start()
        }
        binding.linearBird.apply {
            setOnClickListener {
                if (btnClickSoundId != 0)
                    startSound(btnClickSoundId)
                setSharedData("prefCharacter", "character", LoadingActivity.CHARACTER_BIRD)
                resetColor()
                binding.linearBird.setBackgroundResource(R.drawable.mode_linear_shape_selected)

            }
        }
        binding.choicePig.apply {
            setBackgroundResource(R.drawable.animation_pig)
            val animation = background as AnimationDrawable
            animation.start()
        }
        binding.linearPig.apply {
            setOnClickListener {
                if (btnClickSoundId != 0)
                    startSound(btnClickSoundId)
                setSharedData("prefCharacter", "character", LoadingActivity.CHARACTER_PIG)
                resetColor()
                binding.linearPig.setBackgroundResource(R.drawable.mode_linear_shape_selected)
            }
        }
        binding.choiceGhost.apply {
            setBackgroundResource(R.drawable.animation_ghost)
            val animation = background as AnimationDrawable
            animation.start()
        }
        binding.linearGhost.apply {
            setOnClickListener {
                if (btnClickSoundId != 0)
                    startSound(btnClickSoundId)
                setSharedData("prefCharacter", "character", LoadingActivity.CHARACTER_GHOST)
                resetColor()
                binding.linearGhost.setBackgroundResource(R.drawable.mode_linear_shape_selected)

            }
        }

        val name = getSharedStringData("prefUserName", "userName")
        binding.currentUserName.text = name

        binding.btnBack.setOnClickListener {
            finish()
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
        btnClickSoundId = soundPool.load(applicationContext, R.raw.bgm_click, 1)
    }
    
    fun setSharedData(name: String, key: String, data: Int) {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putInt(key, data)
        editor.apply()
    }
    fun getSharedIntData(name: String, key: String) : Int {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        return pref.getInt(key, 0)
    }
    fun setSharedData(name: String, key: String, data: String) {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString(key, data)
        editor.apply()
    }
    fun getSharedStringData(name: String, key: String) : String? {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        return pref.getString(key, "안광민")
    }
    fun resetColor() {
        binding.linearBird.setBackgroundResource(R.drawable.mode_linear_shape)
        binding.linearRabbit.setBackgroundResource(R.drawable.mode_linear_shape)
        binding.linearPig.setBackgroundResource(R.drawable.mode_linear_shape)
        binding.linearGhost.setBackgroundResource(R.drawable.mode_linear_shape)
    }
    fun startSound(id: Int) {
        soundPool.play(id, 1f, 1f,
            1, 0, 1f)
    }

}