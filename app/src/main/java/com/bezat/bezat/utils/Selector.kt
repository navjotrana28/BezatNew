package com.bezat.bezat.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bezat.bezat.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.spinner_item.view.*

class Selector<T>(
    context: Context,
    val list: List<T>,
    val getTitle: (T) -> String,
    val getImage: (T) -> String,
    val action: (T, String) -> Unit
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = RecyclerView(context)
        initRoot(root)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(root)
    }

    fun initRoot(root: RecyclerView) {
        root.layoutManager = LinearLayoutManager(root.context)
        root.addLineDecorator(context, 0)
        root.adapter =
            SelectorAdapter(list, getImage, getTitle, { p0, p1 -> action(p0, p1); dismiss() })
    }
}

private class SelectorAdapter<T>(
    val list: List<T>,
    val image: (T) -> String, val getString: (T) -> String, val action: (T, String) -> Unit
) : RecyclerView.Adapter<SelectorViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SelectorViewHolder {
        return SelectorViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.spinner_item,
                p0,
                false
            )
        ).apply {
            view.setOnClickListener {
                action(list[adapterPosition], textView.text.toString())
            }
        }
    }

    override fun getItemCount() = list.count()

    override fun onBindViewHolder(p0: SelectorViewHolder, p1: Int) {
        p0.textView.text = getString(list[p1])
        Picasso.get().load(image(list[p1])).into(p0.imageView)
    }

}

class SelectorViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val textView = view.text1 as TextView
    val imageView = view.countryIcon as ImageView
}