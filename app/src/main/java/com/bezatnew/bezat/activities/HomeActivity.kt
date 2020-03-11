package com.bezatnew.bezat.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bezatnew.bezat.R

class HomeActivity : AppCompatActivity() {

//    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.navigation_dashboard -> {
//                tabs.currentItem = 0
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_bell -> {
//                tabs.currentItem = 1
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_profile -> {
//                tabs.currentItem = 2
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_settings -> {
//                tabs.currentItem = 3
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        initNavigation()
//        initViewPager()
    }

//    fun initNavigation(){
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//    }

//    fun initViewPager() {
//        tabs.offscreenPageLimit = 3
//        tabs.adapter = TabAdapter(supportFragmentManager).apply {
//            addFragment(Dashboard())
//            addFragment(Notification())
//            addFragment(MyProfile())
//            addFragment(Settings())
//            tabs.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
//                override fun onPageScrollStateChanged(p0: Int) {
//                }
//
//                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
//                }
//
//                override fun onPageSelected(p0: Int) {
//
//                    navigation.selectedItemId = when (p0){
//                        0 -> R.id.navigation_dashboard
//                        1 -> R.id.navigation_bell
//                        2 -> R.id.navigation_profile
//                        3 -> R.id.navigation_settings
//                        else -> -1
//                    }
//                }
//            }
//            )
//        }
//    }
}