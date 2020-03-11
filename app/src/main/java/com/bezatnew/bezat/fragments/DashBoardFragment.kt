package com.bezatnew.bezat.fragments

import android.graphics.Rect
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bezatnew.bezat.R
import com.bezatnew.bezat.activities.TabAdapter
import com.bezatnew.bezat.api.BannerOuterRequest
import com.bezatnew.bezat.api.BannerOuterResponse
import com.bezatnew.bezat.utils.PreferenceManager
import kotlinx.android.synthetic.main.fragmen_dashboard.view.*
import kotlinx.android.synthetic.main.row_dashboard_item.view.*

class DashBoardFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragmen_dashboard, container, false)
        initUI(root)
        return root
    }

    fun initUI(root: View) {
        BannerOuterRequest().request({
            initImageSlider(root.imageSlider, it)
        }, {
        },"http://bindu.biz/bezatapi/user/banners")
        root.rvDashboard.layoutManager = GridLayoutManager(root.context, 3)
        root.rvDashboard.adapter = getAdapter()
        root.rvDashboard.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val value =
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, activity!!.resources.displayMetrics)
                        .toInt()
                outRect.top = value
                outRect.bottom = value
                outRect.left = value
                outRect.right = value
            }

        })
    }

    private fun initImageSlider(view: ViewPager, response: BannerOuterResponse){
            val fragments = mutableListOf<Fragment>()
            for(i in response.result)
                fragments.add(ImageFragment.getFragmet(i.banner))
            view.adapter = TabAdapter(fragmentManager!!).apply { for(i in fragments) addFragment(i) }
        }

    private fun getAdapter(): RecyclerView.Adapter<*>? {
        val list= mutableListOf<DashBoardItem>()
        list.add(
            DashBoardItem(
                R.drawable.get_coupon,
                R.string.get_coupon
            ) {})
        list.add(
            DashBoardItem(
                R.drawable.fav_offers,
                R.string.fav_offers
            ) {})
        list.add(
            DashBoardItem(
                R.drawable.prizes,
                R.string.prizes
            ) {})
        list.add(
            DashBoardItem(
                R.drawable.total_coupon,
                R.string.total_coupon
            ) {})
        list.add(
            DashBoardItem(
                R.drawable.partners,
                R.string.partners
            ) {})
        list.add(
            DashBoardItem(
                R.drawable.winners,
                R.string.winners
            ) {})
        list.add(
            DashBoardItem(
                R.drawable.my_profile,
                R.string.my_profile
            ) {})
        list.add(
            DashBoardItem(
                R.drawable.vip_offers,
                R.string.vip_offers
            ) {})
        list.add(
            DashBoardItem(
                R.drawable.logout,
                R.string.sign_out
            ) {
                PreferenceManager.instance.logOut(activity)
            })
        val adapter = DashBoardAdapter(list)

        return adapter
    }
}
data class DashBoardItem(
    @DrawableRes val image:Int,
    @StringRes val text:Int,
    val action:()->Unit
)
class DashBoardAdapter(val list: MutableList<DashBoardItem>): RecyclerView.Adapter<DashBoardViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): DashBoardViewHolder {
        return DashBoardViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.row_dashboard_item,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(p0: DashBoardViewHolder, p1: Int) {
        list[p1].run {
            p0.image.setImageResource(image)
            p0.text.setText(text)
            p0.itemView.setOnClickListener { action() }
        }
    }

}
class DashBoardViewHolder(view:View): RecyclerView.ViewHolder(view){
    val image = view.image
    val text = view.text
}