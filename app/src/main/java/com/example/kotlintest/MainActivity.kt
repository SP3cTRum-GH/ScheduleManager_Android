package com.example.kotlintest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlintest.calendar.Calendar
import com.example.kotlintest.settingplanner.SettingPlanner
import com.example.kotlintest.home.Home
import com.example.kotlintest.setting.Setting
import com.example.kotlintest.livedata.LiveDataFactory
import com.example.kotlintest.livedata.PlannerLivedata
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var _plannerLivedata: PlannerLivedata
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var frag: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _plannerLivedata = ViewModelProvider(this, LiveDataFactory(applicationContext)).get(PlannerLivedata::class.java)


        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavi)
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, Home(_plannerLivedata)).commit()
        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
//                    _calLivedata.getAllTaskForDate(LocalDate.now().toString())
                    frag = Home(_plannerLivedata)
                }
                R.id.calinder -> {
                    frag = Calendar()
                }
                R.id.detailPlannerName -> {
                    frag = SettingPlanner(_plannerLivedata)
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