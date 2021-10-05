package com.ssacproject.fourthweek.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.ssacproject.fourthweek.*
import com.ssacproject.fourthweek.databinding.ActivityBoardBinding

class BoardActivity : AppCompatActivity() {
    lateinit var binding: ActivityBoardBinding
    var tabTitle = listOf<String>("EASY", "NORMAL", "HARD")
    var fragmentList = listOf(getFragmentMode("EASY"), getFragmentMode("NORMAL"), getFragmentMode("HARD"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        setContentView(binding.root)

        var fragmentAdapter = ViewPagerFragmentAdapter(this)
        fragmentAdapter.fragmentList = fragmentList

        binding.boardViewPager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = fragmentAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    val view = fragmentList[position].view
                    if (view != null) {
                        val wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view!!.width, View.MeasureSpec.EXACTLY)
                        val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        view.measure(wMeasureSpec, hMeasureSpec)

                        if (layoutParams.height != view.measuredHeight) {
                            layoutParams = layoutParams.also { lp ->
                                lp.height = view.measuredHeight
                            }
                        }

                    }

                }
            })
        }
        TabLayoutMediator(binding.boardTabLayout, binding.boardViewPager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    fun getFragmentMode(mode: String) : FragmentMode {
        val fragment = FragmentMode()
        var bundle = Bundle()
        bundle.putString("mode", mode)
        fragment.arguments = bundle
        return fragment

    }

}