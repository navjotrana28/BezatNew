package com.bezatnew.bezat.utils

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.InsetDrawable
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText
import com.bezatnew.bezat.R
import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class Utils {
    companion object {
    }
}

fun RecyclerView.addLineDecorator(
    context: Context,
    inset: Int = (context.resources.getDimension(R.dimen.default_margin) / 2f).toInt()
) {
    addItemDecoration(
        DividerItemDecoration(
            context,
            RecyclerView.VERTICAL
        ).apply {
            setDrawable(
                InsetDrawable(
                    ContextCompat.getDrawable(context, R.drawable.div_primary)!!, inset, 0, inset, 0
                )
            )
        })
}

fun <T> EditText.convertToSpinner(
    selection: List<T>,
    getDisplayText: (T) -> String,
    selectionImage: (T) -> String,
    setImageOnClick: (T) -> Unit,
    postAction: ((T, String) -> Unit)? = null
) {
    keyListener = null
    val dialog = Selector(
        context,
        selection,
        getDisplayText,
        selectionImage,
        setImageOnClick,
        { obj, text -> setText(text); error = null; postAction?.invoke(obj, text) })
    setOnClickListener { dialog.show() }
    setOnFocusChangeListener { _, hasFocus -> if (hasFocus) dialog.show() }
}

fun EditText.convertToDatePicker(postAction: ((String) -> Unit)? = null) {
    keyListener = null
    val d = Calendar.getInstance()
    val dialog = DatePickerDialog(context, { _, y, mm, d ->
        val m = mm + 1;
        val tmp =
            "$y-${if (m > 9) "$m" else "0$m"}-${if (d < 10) "0$d" else "$d"}";setText(tmp); error =
        null; postAction?.invoke(tmp)
    }, 1997, 1, 1)
    setOnClickListener { dialog.show() }
    setOnFocusChangeListener { _, hasFocus -> if (hasFocus) dialog.show() }
}

fun String.isPhoneValid(): Boolean {
    return try {
        val util = PhoneNumberUtil.getInstance()
        util.isValidNumber(util.parse("+${this.substring(2)}", null))
    } catch (e: Exception) {
        false
    }
}

fun Context.showMessage(message: String = getString(R.string.someting_wrong)) {
    AlertDialog.Builder(this).setMessage(message).setPositiveButton(R.string.ok) { d, _ ->
        d.dismiss()
    }.create().show()
}

fun String.toList(): MutableList<Pair<String, *>> {
    val p = JSONObject(this)
    val list = mutableListOf<Pair<String, *>>()
    for (i in p.keys()) {
        list.add(i to p.get(i))
    }
    return list
}