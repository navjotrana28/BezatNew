package com.bezatnew.bezat.fragments

import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageFragment: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = ImageView(inflater.context)
        root.scaleType = ImageView.ScaleType.CENTER_CROP
        val res = arguments?.getInt(IMAGE)
        if(res!=null && res != 0)
        root.setImageResource(res)
        else
            Glide.with(activity!!).load(arguments?.getString(IMAGE_STR)).into(root)
        return root
    }
    companion object {
        const val IMAGE = "IMAGE"
        const val IMAGE_STR = "IMAGE_STR"
       fun  getFragmet(@DrawableRes image:Int): Fragment {
           val fragment = ImageFragment()
           fragment.arguments = Bundle().apply { putInt(IMAGE, image) }
           return fragment
       }
       fun  getFragmet( image:String): Fragment {
           val fragment = ImageFragment()
           fragment.arguments = Bundle().apply { putString(IMAGE_STR, image) }
           return fragment
       }
    }
}