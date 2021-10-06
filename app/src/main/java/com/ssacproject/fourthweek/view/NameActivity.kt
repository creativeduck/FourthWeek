package com.ssacproject.fourthweek.view

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.ssacproject.fourthweek.R
import com.ssacproject.fourthweek.databinding.ActivityNameBinding

class NameActivity : AppCompatActivity() {
    lateinit var binding: ActivityNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNameBinding.inflate(layoutInflater)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContentView(binding.root)

        binding.editUserName.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionid: Int, event: KeyEvent?): Boolean {
                if (actionid == EditorInfo.IME_ACTION_DONE) {
                    val text = binding.editUserName.text.toString()

                    setSharedData("prefUserName", "userName", text)

                    val inputMethodManager: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                    return true
                }
                return false
            }
        })

        binding.btnBack.setOnClickListener {
            finish()
        }


    }
    fun setSharedData(name: String, key: String, data: String) {
        val pref : SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = pref.edit()
        editor.putString(key, data)
        editor.apply()
    }
}