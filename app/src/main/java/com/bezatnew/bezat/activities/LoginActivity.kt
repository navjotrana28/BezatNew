@file:Suppress("DEPRECATION")

package com.bezatnew.bezat.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bezatnew.bezat.R
import com.bezatnew.bezat.api.LoginRequest
import com.bezatnew.bezat.utils.SharedPrefs
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPrefs.getKey(this, "selectedlanguage").contains("ar")) {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
            setLocale("ar")
        } else {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
            setLocale("en")
        }
        setContentView(R.layout.activity_login)
        initUI()
    }

    fun initUI() {
        skip_button.setOnClickListener {
            val intent = Intent(this, Homepage::class.java)
            SharedPrefs.setGuestUser(this, true)
            startActivity(intent)
        }
        btnLogin.setOnClickListener {
            doLogin()
        }
        tvSignUp.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        txtForgot.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
    }

    private fun showMessage(message: String = getString(R.string.someting_wrong)) {
        AlertDialog.Builder(this).setMessage(message).setPositiveButton(R.string.ok) { d, _ ->
            d.dismiss()
        }.create().show()
    }

    private fun isValid(): Boolean {
        var isValid = true
        if (etEmail.text?.isEmpty() != false) {
            isValid = false
            etEmail.error = getString(R.string.empty_email)
        }
        if (etPassword.text?.isEmpty() != false) {
            isValid = false
            etPassword.error = getString(R.string.empty_password)
        }
        return isValid
    }

    private fun doLogin() {
        if (isValid())
            LoginRequest(
                email = etEmail.text.toString(),
                password = etPassword.text.toString()
            ).login(
                this, {
                    it.handleLogin(this, {

                        SharedPrefs.setKey(this@LoginActivity, "userId", it.userID);
                        SharedPrefs.setKey(this@LoginActivity, "LoggedIn", "true");
                        SharedPrefs.setGuestUser(this, false)
                        startActivity(Intent(this@LoginActivity, Homepage::class.java))
                        finishAffinity();
                    }, this::showMessage)
                },
                {
                    showMessage()
                }
            )
    }

    fun setLocale(lang: String) {
        SharedPrefs.setKey(this, "selectedlanguage", lang)
        val myLocale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }
}
