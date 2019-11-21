package com.bezat.bezat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.bezat.bezat.R
import com.bezat.bezat.api.RegisterRequest
import com.bezat.bezat.utils.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (SharedPrefs.getKey(this, "selectedlanguage").contains("ar")) {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
            setLocale("ar")
        } else {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        setContentView(R.layout.activity_registration)
        initUI()
    }
    var code:String? = null
    var date:String? = null
    fun initUI(){
        save.setOnClickListener {
            validateAndSave()
        }
        etDOB.convertToDatePicker {date = it}
        country.convertToSpinner(listOf("Bahrain" to "00973","Saudi Arabia" to "00971", "United Arab Emirates" to "00973"),{it.first}){obj,_ -> code = obj.second}
        etGender.convertToSpinner(listOf("Female","Male"),{it})
    }
    private fun validateAndSave(){
        val request = RegisterRequest(deviceId = PreferenceManager.instance.deviceId, email = etEmail.text.toString() , name = etName.text.toString(), gender = etGender.text.toString(), phone = phone.text.toString(), mobileCode = code?:"", dob = date?:"", password = etPassword.text.toString())
        if(validate(request))
            register(request)
    }
    fun register(request: RegisterRequest) {
     request.register(this,{
         response->
         //response return case
         response.evaluate(this, {
             val otp= response.userInfo?.otp;

             val intent = Intent(this@RegistrationActivity,OTP::class.java)
             intent.putExtra("otp",otp);
             intent.putExtra("deviceId",request.deviceId);
             intent.putExtra("dob",request.dob);
             intent.putExtra("email",request.email);
             intent.putExtra("gender",request.gender);
             intent.putExtra("mobileCode",request.mobileCode);
             intent.putExtra("password",request.password);
             intent.putExtra("phone",request.phone);
             startActivity(intent)
             Toast.makeText(this,getString(R.string.reg_success),Toast.LENGTH_SHORT).show()
             finish()
         }){
         this@RegistrationActivity.showMessage(it)
     }
     })  {
         this.showMessage(it)
         //connection error case
     }
    }
    fun validate(request:RegisterRequest):Boolean {
        var isValid = true
        if (etEmail.text?.isEmpty() != false) {
            isValid = false
            etEmail.error = getString(R.string.empty_email)
        } else
            if (!Patterns.EMAIL_ADDRESS.matcher(request.email).matches()) {
                isValid = false
                etEmail.error = getString(R.string.valid_email)
            }
        if (etName.text?.isEmpty() != false) {
            isValid = false
            etName.error = getString(R.string.empty_name)
        }
        var flag = 2
        if (etPassword.text?.isEmpty() != false) {
            isValid = false
            etPassword.error = getString(R.string.empty_password)
            flag--
        }
        if (etConfirmPassword.text?.isEmpty() != false) {
            isValid = false
            etConfirmPassword.error = getString(R.string.empty_confirm_password)
            flag--
        }
        if (flag == 2 && request.password != etConfirmPassword.text.toString()) {
            isValid = false
            etConfirmPassword.error = getString(R.string.password_mismatch)
        }
        if (request.dob.isEmpty()) {
            isValid = false
            etDOB.error = getString(R.string.empty_dob)
        }
        if (request.phone.isEmpty()) {
            isValid = false
            phone.error = getString(R.string.empty_phone)
            flag = 3
        }
        if(flag != 3 && !(request.mobileCode + request.phone).isPhoneValid()) {
            isValid = false
            phone.error = getString(R.string.invalid_phone)
        }
        if(etGender?.text?.isBlank() != false)
        {
            isValid = false
            etGender.error = getString(R.string.empty_gender)
        }
        if(country?.text?.isBlank() != false)
        {
            isValid = false
            country.error = getString(R.string.empty_country)
        }
        return isValid
    }

    fun setLocale(lang: String) {
        val myLocale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

    }
}
