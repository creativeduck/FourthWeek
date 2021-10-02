package com.ssacproject.fourthweek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssacproject.fourthweek.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}