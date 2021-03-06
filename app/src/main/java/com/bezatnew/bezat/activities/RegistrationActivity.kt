package com.bezatnew.bezat.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bezatnew.bezat.ClientRetrofit
import com.bezatnew.bezat.MyApplication
import com.bezatnew.bezat.R
import com.bezatnew.bezat.activities.RegistrationActivity.PostAdapter.MyViewHolder
import com.bezatnew.bezat.api.LoginRequest
import com.bezatnew.bezat.api.RegisterRequest
import com.bezatnew.bezat.interfaces.RegisterUserCallBack
import com.bezatnew.bezat.models.RegisterRequestResponse
import com.bezatnew.bezat.models.RegisterUserRequest
import com.bezatnew.bezat.utils.*
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.etEmail
import kotlinx.android.synthetic.main.activity_registration.etPassword
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class RegistrationActivity : AppCompatActivity(), RegisterUserCallBack {
    private var isOtpValidated: Boolean = false
    internal var loader: Loader? = null
    internal var dialog: Dialog? = null


    internal var postAdapter: PostAdapter? = null

    internal var smsHashCodeHelper = SmsHashCodeHelper(this)

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


    var code: String? = null
    var otp: String? = null
    var date: String? = null
    fun initUI() {
        loader = Loader(this)
        loader!!.dismiss()

        etGender.setOnClickListener {
            //            etGender.convertToSpinner(listOf("Female", "Male"), { "" }, { it }, {})
            val dialog = Dialog(this, R.style.Theme_AppCompat_Light_Dialog)
            dialog.setContentView(R.layout.gender_dialog)
            dialog.show()
            val txtFemale = dialog.findViewById(R.id.txtFemale) as TextView
            val txtMale = dialog.findViewById(R.id.txtMale) as TextView

            txtFemale.setOnClickListener {
                etGender.setText("Female")
                dialog.dismiss()
            }
            txtMale.setOnClickListener {
                etGender.setText("Male")
                dialog.dismiss()
            }
        }

        save.setOnClickListener {
            //            loader.show()
            if (!isOtpValidated) {

                validateForOtpAndSave()
//                verifyAge();
            } else {
                validateAndSave()
//                verifyAge();
            }
        }

        etDOB.convertToDatePicker { date = it }

//        GetCountryService().getCountries({ countryData ->
//            country.convertToSpinner(
//                countryData.result, { it.name }, { it.phoneCode }, {
//                    Picasso.get().load(it.img).into(countryIcon)
//                }) { obj, _ -> code = obj.phoneCode }
//        }, {})
        getCountryList()
        country.setOnClickListener {
            showDialog()
        }
    }

    private fun getCountryList() {
        val `object` = JSONObject()
        val Url = URLS.GET_COUNTRY
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,
            Url,
            `object`,
            { response ->
                loader?.dismiss()
                try {
                    postAdapter = PostAdapter(response.getJSONArray("result"))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error -> loader?.dismiss() }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["apikey"] = "12345678"
                return headers
            }
        }

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest)
    }

    private fun showDialog() {
        dialog = Dialog(this@RegistrationActivity)
        dialog!!.setContentView(R.layout.country_dialog)
        dialog!!.show()
        val recCountry = dialog!!.findViewById<RecyclerView>(R.id.recCountry)
        val layoutManager = StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        recCountry.setLayoutManager(layoutManager)
        recCountry.setItemAnimator(DefaultItemAnimator())
        if (postAdapter != null && postAdapter!!.getItemCount() > 0) {
            recCountry.setAdapter(postAdapter)
        }
    }

    inner class PostAdapter(internal var jsonArray: JSONArray) :
        RecyclerView.Adapter<MyViewHolder>() {

        fun append(array: JSONArray) {
            try {
                for (i in 0 until array.length()) {
                    this.jsonArray.put(array.get(i))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.country_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            try {


                holder.txtCountry.text = jsonArray.getJSONObject(position).getString("name")
                holder.txtCode.text = jsonArray.getJSONObject(position).getString("phone_code")
                holder.txtCountryCode.text =
                    "(" + jsonArray.getJSONObject(position).getString("country_code") + ")"
                Picasso.get().load(jsonArray.getJSONObject(position).getString("img"))
                    .into(holder.imgFlag)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun getItemCount(): Int {
            return jsonArray.length()
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            internal var txtCountry: TextView
            internal var txtCode: TextView
            internal var txtCountryCode: TextView
            internal var imgFlag: ImageView


            init {
                imgFlag = itemView.findViewById(R.id.imgFlag)

                txtCountry = itemView.findViewById(R.id.txtCountry)
                txtCode = itemView.findViewById(R.id.txtCode)
                txtCountryCode = itemView.findViewById(R.id.txtCountryCode)


                itemView.setOnClickListener {
                    try {
                        Picasso.get().load(jsonArray.getJSONObject(adapterPosition).getString("img"))
                            .into(countryIcon)
                        country.setText(jsonArray.getJSONObject(adapterPosition).getString("phone_code"))
                        code = country.text.toString()
                        dialog?.dismiss()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

            }
        }

    }


    public fun verifyAge() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Verification")
        builder.setMessage("Please verify you are above 17 years old")
        builder.setPositiveButton("Verify", { dialogInterface: DialogInterface, i: Int -> })
        builder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }

    private fun validateForOtpAndSave() {
        val request = RegisterUserRequest()
        request.mobile_code = code ?: ""
        request.password = etPassword.text.toString()
        request.phone = phone.text.toString()
        val hashCode = smsHashCodeHelper.appHashCode.toString()
        request.smsHashCode = hashCode.substring(1, hashCode.length - 1)

        if (validateForOtp(request)) {
            registerUser(request)
        } else {
            val loader = Loader(this);
            loader.dismiss()
        }
    }

    private fun registerUser(request: RegisterUserRequest) {
        val clientRetrofit = ClientRetrofit()
        clientRetrofit.registerUser(request, this)
    }

    private fun validateAndSave() {
        var request = RegisterRequest(
            os = "Android",
            deviceId = PreferenceManager.instance.deviceId,
            email = etEmail.text.toString(),
            name = etName.text.toString(),
            gender = etGender.text.toString(),
//            phone = phone.text.toString(),
//            mobileCode = code ?: "",
            dob = date ?: "",
//            password = etPassword.text.toString(),
            userID = SharedPrefs.getKey(this, "userId")
        )
        if (validate(request))
            register(request)
    }

    override fun onResponse(response: RegisterRequestResponse?) {
        if (response?.status.equals("success")) {
            val loader = Loader(this)
            loader.dismiss()
            if (response != null) {
                SharedPrefs.setKey(this, "userId", response.userID.toString())
            }
            otp = response?.userInfo!!.otp.toString()
            Log.d("otp=", otp)
            val intent = Intent(this@RegistrationActivity, OTP::class.java)
            intent.putExtra("otp", otp)
            intent.putExtra("deviceId", PreferenceManager.instance.deviceId)
            intent.putExtra("mobileCode", response?.userInfo?.phoneCode)
            intent.putExtra("password", etPassword.text.toString())
            intent.putExtra("phone", phone.text.toString())
            intent.putExtra("dob", "")
            intent.putExtra("email", "")
            intent.putExtra("gender", "")
            intent.putExtra("os", "")
            startActivityForResult(intent, 0)
        } else {
            if (response != null) {
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setMessage(response.error_msg.toString())
                    .setNegativeButton(R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
//                Toast.makeText(this, response.error_msg.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onError(throwable: Throwable?) {
        this.showMessage(throwable?.message.toString())
    }

    fun register(request: RegisterRequest) {
        request.register(this, { response ->
            //response return case
            response.evaluate(this, {
                //                otpVerification()
                doLogin()
                Toast.makeText(this, getString(R.string.reg_success), Toast.LENGTH_SHORT).show()
            }) {
                this@RegistrationActivity.showMessage(it)
            }
        }) {
            this.showMessage(it)
            //connection error case
        }
    }

    private fun doLogin() {
        LoginRequest(
            email = etEmail.text.toString(),
            password = etPassword.text.toString(),
            os = "Android"
        ).loginReg(
            this, {
                it.handleLoginReg(this, {

                    SharedPrefs.setKey(this@RegistrationActivity, "userId", it.userID);
                    SharedPrefs.setKey(this@RegistrationActivity, "LoggedIn", "true");
                    SharedPrefs.setGuestUser(this, false)
                    startActivity(Intent(this@RegistrationActivity, Homepage::class.java))
                    finishAffinity();
                }, this::showMessage)
            },
            {
                showMessage()
            }
        )

    }

    private fun showAllView() {
        Handler().postDelayed({
            findViewById<TextInputLayout>(R.id.name_edit_text).visibility = View.VISIBLE
            findViewById<TextInputLayout>(R.id.gender_edit_text).visibility = View.VISIBLE
            findViewById<TextInputLayout>(R.id.email_edit_text).visibility = View.VISIBLE
            findViewById<TextInputLayout>(R.id.date_of_birth_edit_text).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_verify).visibility = View.VISIBLE
        }, 250)
    }

    fun validate(request: RegisterRequest): Boolean {
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

        if (request.dob.isEmpty()) {
            isValid = false
            etDOB.error = getString(R.string.empty_dob)
        }

        if (etGender?.text?.isBlank() != false) {
            isValid = false
            etGender.error = getString(R.string.empty_gender)
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

    private fun validateForOtp(request: RegisterUserRequest): Boolean {
        var isValid = true
        var flag = 2
        if (!isOtpValidated) {
            if (country?.text?.isBlank() != false) {
                isValid = false
                country.error = getString(R.string.empty_country)
            }
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
            if (request.phone.isEmpty()) {
                isValid = false
                phone.error = getString(R.string.empty_phone)
            }
            if (
//               (request.mobile_code + request.phone).isPhoneValid() ||
                (request.mobile_code + request.phone).length <= 7) {
                isValid = false
                phone.error = getString(R.string.invalid_phone)
            }
        }
        return isValid
    }


    private fun otpVerification() {
        val loader = Loader(this)
//        loader.show()

        val volleyMultipartRequest =
            object : VolleyMultipartRequest(Request.Method.POST, URLS.OTP_VALIDATION,
                Response.Listener { response ->
                    loader.dismiss()
                    val res = String(response.data)
                    try {
                        val jsonObject = JSONObject(res)
                        if (jsonObject.getString("status").equals("success", ignoreCase = true)) {
                            finish()
                            Toast.makeText(
                                this,
                                jsonObject.getString("success_msg"), Toast.LENGTH_LONG
                            ).show()

                        } else {
                            finish()
                            Toast.makeText(
                                this,
                                jsonObject.getString("error_msg"), Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { error ->
                    loader.dismiss()
                    val json: String? = null
                    val Message: String
                    val response = error.networkResponse
                    Log.v("response", response.data.toString() + "")
                }) {
                @Throws(AuthFailureError::class)
                public override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["phone"] = phone.toString()
                    params["otp_code"] = otp.toString()
                    return params
                }

                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["apikey"] = "12345678"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getByteData(): Map<String, VolleyMultipartRequest.DataPart>? {
                    return HashMap()
                }
            }
        MyApplication.getInstance().addToRequestQueue(volleyMultipartRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    isOtpValidated = it.getBooleanExtra(OTP.IS_OTP_VERIFIED, false)
                    if (isOtpValidated) {
                        showAllView()
                        val loader = Loader(this)
                        loader.dismiss()
                    }
                }
            } else {
                onBackPressed()
            }
        } else {
            onBackPressed()
        }
    }
}
