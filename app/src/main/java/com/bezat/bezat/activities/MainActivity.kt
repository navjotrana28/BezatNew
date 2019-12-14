package com.bezat.bezat.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bezat.bezat.utils.SharedPrefs
import com.bezat.bezat.utils.URLS
import com.github.kittinunf.fuel.core.FuelManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPrefs.getKey(this, "selectedlanguage").contains("ar")) {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        } else {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (SharedPrefs.getKey(this@MainActivity, "LoggedIn").equals("true")) {
                startActivity(Intent(this, Homepage::class.java))
            } else {
                startActivity(Intent(this, Homepage::class.java))

//                startActivity(Intent(this, Intro::class.java))

            }
            finish()
        }, 3000)
        FuelManager.instance.apply {
            basePath = URLS.BASE_PATH
            baseHeaders = mapOf("apiKey" to "12345678")
        }
    }
}
