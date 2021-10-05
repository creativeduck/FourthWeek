package com.ssacproject.fourthweek.view

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.room.Room
import com.ssacproject.fourthweek.R
import com.ssacproject.fourthweek.databinding.ActivityLoadingBinding
import com.ssacproject.fourthweek.room.*
import kotlinx.coroutines.*

class LoadingActivity : AppCompatActivity() {
    companion object {
        var boardHelper: RoomBoardHelper? = null
        const val CHARACTER_RABBIT = 0
        const val CHARACTER_BIRD = 1
        const val CHARACTER_PIG = 2
        const val CHARACTER_GHOST = 3
    }

    lateinit var binding: ActivityLoadingBinding
    lateinit var animationDrawable: AnimationDrawable
    lateinit var mediaPlayer: MediaPlayer
    var currentState = 0
    var onPlay = false
    var rabbitList = arrayOfNulls<Bitmap>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContentView(binding.root)

        boardHelper = Room.databaseBuilder(this, RoomBoardHelper::class.java, "room_board3")
            .build()

        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, ChooseLevelActivity::class.java)
            startActivity(intent)
        }
        binding.btnBoard.setOnClickListener {
            val intent = Intent(this, BoardActivity::class.java)
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
            val intent = Intent(this, NameActivity::class.java)
            startActivity(intent)
        }

        rabbitList[0] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_down)
        rabbitList[0] = Bitmap.createScaledBitmap(rabbitList[0]!!, 100, 100, false)
        rabbitList[1] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_normal)
        rabbitList[1] = Bitmap.createScaledBitmap(rabbitList[1]!!, 100, 100, false)
        rabbitList[2] = BitmapFactory.decodeResource(resources, R.drawable.rabbit_up)
        rabbitList[2] = Bitmap.createScaledBitmap(rabbitList[2]!!, 100, 100, false)
    }

    override fun onStart() {
        super.onStart()
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm_elinia)
        mediaPlayer.isLooping = true
        binding.btnSoundControl.setImageResource(R.drawable.ic_onplay)

        binding.characterLoading.apply {
            val character = getSharedIntData("prefCharacter", "character")
            when (character) {
                CHARACTER_BIRD -> setBackgroundResource(R.drawable.animation_bird)
                CHARACTER_PIG -> setBackgroundResource(R.drawable.animation_pig)
                CHARACTER_GHOST -> setBackgroundResource(R.drawable.animation_ghost)
                else -> setBackgroundResource(R.drawable.animation_rabbit)
            }
            animationDrawable = background as AnimationDrawable
            animationDrawable.start()
        }
        val name = getSharedStringData("prefUserName", "userName")
        binding.userName.text = name

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

    fun getSharedIntData(name: String, key: String) : Int {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        return pref.getInt(key, 0)
    }
    fun getSharedStringData(name: String, key: String) : String? {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        return pref.getString(key, "안광민")
    }

}