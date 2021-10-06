package com.ssacproject.fourthweek.view

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ssacproject.fourthweek.R
import com.ssacproject.fourthweek.databinding.ActivityChooseLevelBinding

class ChooseLevelActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseLevelBinding

    companion object {
        const val MODE_EASY = 0
        const val MODE_NORMAL = 1
        const val MODE_HARD = 2
    }

    var btnClickSoundId = 0
    lateinit var soundPool: SoundPool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseLevelBinding.inflate(layoutInflater)
        
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        setContentView(binding.root)
        val intent = Intent(this, MainActivity::class.java)
        binding.modeEasy.setOnClickListener {
            if (btnClickSoundId != 0)
                startSound(btnClickSoundId)
            intent.putExtra("mode", MODE_EASY)
            startActivity(intent)
            finish()
        }
        binding.btnModeEasy.setOnClickListener {
            if (btnClickSoundId != 0)
                startSound(btnClickSoundId)
            intent.putExtra("mode", MODE_EASY)
            startActivity(intent)
            finish()
        }

        binding.modeNormal.setOnClickListener {
            if (btnClickSoundId != 0)
                startSound(btnClickSoundId)
            intent.putExtra("mode", MODE_NORMAL)
            startActivity(intent)
            finish()
        }
        binding.btnModeNormal.setOnClickListener {
            if (btnClickSoundId != 0)
                startSound(btnClickSoundId)
            intent.putExtra("mode", MODE_NORMAL)
            startActivity(intent)
            finish()
        }
        binding.modeHard.setOnClickListener {
            if (btnClickSoundId != 0)
                startSound(btnClickSoundId)
            intent.putExtra("mode", MODE_HARD)
            startActivity(intent)
            finish()
        }
        binding.btnModeHard.setOnClickListener {
            if (btnClickSoundId != 0)
                startSound(btnClickSoundId)
            intent.putExtra("mode", MODE_HARD)
            startActivity(intent)
            finish()
        }
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
    fun startSound(id: Int) {
        soundPool.play(id, 1f, 1f,
            1, 0, 1f)
    }
}