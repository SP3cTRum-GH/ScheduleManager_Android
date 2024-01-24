package com.example.kotlintest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.kotlintest.calendar.Calendar
import com.example.kotlintest.goal.Goal
import com.example.kotlintest.home.Home
import com.example.kotlintest.setting.Setting
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var frag: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavi)
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, Home()).commit()
        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    frag = Home()
                }
                R.id.calinder -> {
                    frag = Calendar()
                }
                R.id.goal -> {
                    frag = Goal()
                }
                R.id.setting -> {
                    frag = Setting()
                }
            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, frag)
                .commit()

            true
        }
        bottomNavigationView.selectedItemId = R.id.home
    }
}