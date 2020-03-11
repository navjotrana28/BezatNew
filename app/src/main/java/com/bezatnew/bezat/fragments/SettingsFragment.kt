package com.bezatnew.bezat.fragments

import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bezatnew.bezat.R
import com.bezatnew.bezat.utils.PreferenceManager
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        initUI(root)


        return root
    }
    fun initUI(root:View) {
        root.rvSettings.run {
            layoutManager = LinearLayoutManager(activity!!)
            val inset = (activity!!.resources.getDimension(R.dimen.default_margin) / 2f).toInt()
            addItemDecoration(
                DividerItemDecoration(
                    activity!!,
                    RecyclerView.VERTICAL
                ).apply {
                    setDrawable(
                        InsetDrawable(
                            ContextCompat.getDrawable(context, R.drawable.div_primary)!!,
                            inset,
                            0,
                            inset,
                            0
                        )
                    )
                })
            adapter = this@SettingsFragment.getAdapter()
        }
    }
    fun getAdapter(): RecyclerView.Adapter<SettingsViewHolder>{
        val list = mutableListOf<SettingsItem>()
        list.add(SettingsItem(R.string.my_scan_history){_,_->})
        list.add(SettingsItem(R.string.about_bezat){_,_->})
        list.add(SettingsItem(R.string.terms_conditions){_,_->})
        list.add(SettingsItem(R.string.privacy_policy){_,_->})
        list.add(SettingsItem(R.string.contact_us){_,_->})
        list.add(SettingsItem(R.string.faq){_,_->})
        list.add(SettingsItem(R.string.change_password){_,_->})
        list.add(SettingsItem(R.string.change_language){_,_->})
        list.add(SettingsItem(R.string.push_notification,true){_,_-> PreferenceManager.instance.logOut(activity)})
        list.add(SettingsItem(R.string.logout){_,_->})

        return SettingsAdapter(list)
    }
}
fun callMyScanHistory()
{

//    val newFragment = Notification()
//    val transaction = F.beginTransaction()
//    transaction.replace(R.id.container, newFragment)
//    transaction.addToBackStack(null)
//    transaction.commit()
//    val fragment2 = Notification()
//    val fragmentManager = supportFragmentManager
//    val fragmentTransaction = fragmentManager.beginTransaction()
//    fragmentTransaction.replace(R.id.container, fragment2)
//    fragmentTransaction.addToBackStack(null)
//    fragmentTransaction.commit()
}
class SettingsViewHolder(val view:View): RecyclerView.ViewHolder(view)
{

}

data class SettingsItem(
    @StringRes val title:Int,
    val checkable : Boolean = false,
    val action: (Context, Boolean)->Unit
)

class SettingsAdapter(val list:MutableList<SettingsItem>): RecyclerView.Adapter<SettingsViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SettingsViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val view = if(p1 == 0) {
            inflater.inflate(R.layout.row_settings_switch, p0, false)
        }
        else{
                inflater.inflate(R.layout.row_settings_text,p0,false)
        }
        val holder = SettingsViewHolder(
            (
                    view

                    )
        )
        if (view is Switch) {
            view.setOnCheckedChangeListener { buttonView, isChecked ->
                list[holder.adapterPosition].action(
                    buttonView.context,
                    isChecked
                )
            }
        } else {
            view.setOnClickListener {
               Log.v("onclick", list[holder.adapterPosition].title.toString());
                list[holder.adapterPosition].action(view.context, true) }
        }
        return holder

    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].checkable) 0 else 1
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(p0: SettingsViewHolder, p1: Int) {

        val tmp = list[p1]

        (p0.view as TextView).apply {
                setText(tmp.title)
                setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
                val dim = context.resources.getDimension(R.dimen.default_margin).toInt()
                setPadding(dim, dim, dim, dim)
            }
    }

}