package com.bezatnew.bezat.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.bezatnew.bezat.R
import com.bezatnew.bezat.api.BannerOuterRequest
import com.bezatnew.bezat.api.BannerOuterResponse
import com.bezatnew.bezat.fragments.ImageFragment
import com.bezatnew.bezat.utils.PreferenceManager
import kotlinx.android.synthetic.main.activity_ad.*

class AdActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)
        initUI()
    }
    private fun initUI() {
        BannerOuterRequest().request({
            initImageSlider(imageSlider, it)
        }, {
        })
        btnNext.setOnClickListener { openHome() }
        btnSkip.setOnClickListener { openHome() }
    }

    private fun openHome(){
        if(PreferenceManager.instance.userInfo==null)
        startActivity(Intent(this,LoginActivity::class.java))
        else
        startActivity(Intent(this,Homepage::class.java))
        finish()
    }
    private fun initImageSlider(view: ViewPager, response: BannerOuterResponse){
        val fragments = mutableListOf<Fragment>()
        for(i in response.result)
            fragments.add(ImageFragment.getFragmet(i.banner))
        view.adapter = TabAdapter(supportFragmentManager!!).apply { for(i in fragments) addFragment(i) }
        view.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPageSelected(p0: Int) {
                btnNext.visibility = if(p0 == 1) View.VISIBLE else View.GONE
            }

        })
    }
}