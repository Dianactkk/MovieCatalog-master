package com.diana.moviecatalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.diana.moviecatalog.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPagerAdapter: ViewPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up ViewPagerAdapter and TabLayoutMediator
        viewPagerAdapter = ViewPageAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Movie"
                1 -> "TV"
                else -> null
            }
        }.attach()
    }
}