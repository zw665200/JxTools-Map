package com.recovery.tools.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recovery.tools.R
import com.recovery.tools.adapter.DataAdapter
import com.recovery.tools.bean.Resource
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.DBManager
import com.recovery.tools.utils.AppUtil
import com.recovery.tools.utils.Dict
import com.recovery.tools.utils.FileUtil
import com.recovery.tools.utils.ToastUtil
import com.recovery.tools.view.activity.AgreementActivity
import com.recovery.tools.view.activity.CustomerServiceActivity
import com.recovery.tools.view.activity.SettingActivity
import com.recovery.tools.view.base.BaseFragment
import kotlinx.android.synthetic.main.item_function.view.*
import kotlinx.android.synthetic.main.item_function.view.function_icon
import kotlinx.android.synthetic.main.item_home_pic.view.*
import kotlinx.android.synthetic.main.item_province.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class FInland : BaseFragment() {
    private lateinit var provinces: RecyclerView
    private lateinit var vrs: RecyclerView

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.f_inland, container, false)
        provinces = rootView.findViewById(R.id.list_province)
        vrs = rootView.findViewById(R.id.list_vr)
        return rootView
    }

    override fun initData() {
        loadProvince()
        loadFunction()
    }

    override fun click(v: View?) {
    }

    private fun loadProvince() {
        val list = arrayListOf<Resource>()
        list.add(Resource("website", R.drawable.tiananmen, "北京"))
        list.add(Resource("service", R.drawable.tiananmen, "天津"))
        list.add(Resource("privacy", R.drawable.tiananmen, "河北"))
        list.add(Resource("feedback", R.drawable.tiananmen, "山西"))
        list.add(Resource("feedback", R.drawable.tiananmen, "内蒙古"))
        list.add(Resource("feedback", R.drawable.tiananmen, "辽宁"))
        list.add(Resource("feedback", R.drawable.tiananmen, "吉林"))
        list.add(Resource("feedback", R.drawable.tiananmen, "黑龙江"))
        list.add(Resource("feedback", R.drawable.tiananmen, "上海"))
        list.add(Resource("feedback", R.drawable.tiananmen, "江苏"))
        list.add(Resource("feedback", R.drawable.tiananmen, "浙江"))
        list.add(Resource("feedback", R.drawable.tiananmen, "安徽"))
        list.add(Resource("feedback", R.drawable.tiananmen, "福建"))
        list.add(Resource("feedback", R.drawable.tiananmen, "江西"))
        list.add(Resource("feedback", R.drawable.tiananmen, "山东"))
        list.add(Resource("feedback", R.drawable.tiananmen, "河南"))
        list.add(Resource("feedback", R.drawable.tiananmen, "湖北"))
        list.add(Resource("feedback", R.drawable.tiananmen, "湖南"))
        list.add(Resource("feedback", R.drawable.tiananmen, "广东"))
        list.add(Resource("feedback", R.drawable.tiananmen, "广西"))
        list.add(Resource("feedback", R.drawable.tiananmen, "海南"))
        list.add(Resource("feedback", R.drawable.tiananmen, "重庆"))
        list.add(Resource("feedback", R.drawable.tiananmen, "四川"))
        list.add(Resource("feedback", R.drawable.tiananmen, "贵州"))
        list.add(Resource("feedback", R.drawable.tiananmen, "云南"))
        list.add(Resource("feedback", R.drawable.tiananmen, "西藏"))
        list.add(Resource("feedback", R.drawable.tiananmen, "陕西"))
        list.add(Resource("feedback", R.drawable.tiananmen, "甘肃"))
        list.add(Resource("feedback", R.drawable.tiananmen, "青海"))
        list.add(Resource("feedback", R.drawable.tiananmen, "宁夏"))
        list.add(Resource("feedback", R.drawable.tiananmen, "新疆"))

        val mAdapter = DataAdapter.Builder<Resource>()
            .setData(list)
            .setLayoutId(R.layout.item_province)
            .addBindView { itemView, itemData ->
                itemView.province_name.text = itemData.name

                itemView.setOnClickListener {

                }
            }
            .create()

        provinces.layoutManager = LinearLayoutManager(activity!!)
        provinces.adapter = mAdapter
        mAdapter.notifyItemRangeRemoved(0, list.size)
    }

    private fun loadFunction() {
        val list = arrayListOf<Resource>()
        list.add(Resource("website", R.drawable.tiananmen, "北京"))
        list.add(Resource("service", R.drawable.tiananmen, "天津"))
        list.add(Resource("privacy", R.drawable.tiananmen, "河北"))
        list.add(Resource("feedback", R.drawable.tiananmen, "山西"))
        list.add(Resource("feedback", R.drawable.tiananmen, "内蒙古"))
        list.add(Resource("feedback", R.drawable.tiananmen, "辽宁"))
        list.add(Resource("feedback", R.drawable.tiananmen, "吉林"))
        list.add(Resource("feedback", R.drawable.tiananmen, "黑龙江"))
        list.add(Resource("feedback", R.drawable.tiananmen, "上海"))
        list.add(Resource("feedback", R.drawable.tiananmen, "江苏"))
        list.add(Resource("feedback", R.drawable.tiananmen, "浙江"))
        list.add(Resource("feedback", R.drawable.tiananmen, "安徽"))
        list.add(Resource("feedback", R.drawable.tiananmen, "福建"))
        list.add(Resource("feedback", R.drawable.tiananmen, "江西"))
        list.add(Resource("feedback", R.drawable.tiananmen, "山东"))
        list.add(Resource("feedback", R.drawable.tiananmen, "河南"))
        list.add(Resource("feedback", R.drawable.tiananmen, "湖北"))
        list.add(Resource("feedback", R.drawable.tiananmen, "湖南"))
        list.add(Resource("feedback", R.drawable.tiananmen, "广东"))
        list.add(Resource("feedback", R.drawable.tiananmen, "广西"))
        list.add(Resource("feedback", R.drawable.tiananmen, "海南"))
        list.add(Resource("feedback", R.drawable.tiananmen, "重庆"))
        list.add(Resource("feedback", R.drawable.tiananmen, "四川"))
        list.add(Resource("feedback", R.drawable.tiananmen, "贵州"))
        list.add(Resource("feedback", R.drawable.tiananmen, "云南"))
        list.add(Resource("feedback", R.drawable.tiananmen, "西藏"))
        list.add(Resource("feedback", R.drawable.tiananmen, "陕西"))
        list.add(Resource("feedback", R.drawable.tiananmen, "甘肃"))
        list.add(Resource("feedback", R.drawable.tiananmen, "青海"))
        list.add(Resource("feedback", R.drawable.tiananmen, "宁夏"))
        list.add(Resource("feedback", R.drawable.tiananmen, "新疆"))

        val mAdapter = DataAdapter.Builder<Resource>()
            .setData(list)
            .setLayoutId(R.layout.item_home_pic)
            .addBindView { itemView, itemData, position ->
                Glide.with(activity!!).load(itemData.icon).into(itemView.pic_src)
                itemView.name.text = itemData.name

                itemView.setOnClickListener {
                    when (position) {
                        0 -> openWebsite()
                        1 -> openUserAgreement()
                        2 -> openPrivacyAgreement()
                        3 -> openFeedback()
                        4 -> clearCache()
                        5 -> aboutUs()
                    }
                }
            }
            .create()

        vrs.layoutManager = GridLayoutManager(activity!!, 2)
        vrs.adapter = mAdapter
        mAdapter.notifyItemRangeRemoved(0, list.size)
    }


    private fun openWebsite() {
        if (Constant.WEBSITE == "") return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WEBSITE))
        startActivity(intent)
    }

    private fun openHelpCenter() {
    }

    private fun openFeedback() {
        val intent = Intent(activity!!, CustomerServiceActivity::class.java)
        startActivity(intent)
    }

    private fun openUserAgreement() {
        val intent = Intent(activity!!, AgreementActivity::class.java)
        intent.putExtra("index", 0)
        startActivity(intent)
    }

    private fun openPrivacyAgreement() {
        val intent = Intent(activity!!, AgreementActivity::class.java)
        intent.putExtra("index", 1)
        startActivity(intent)
    }

    private fun openSetting() {
        val intent = Intent(activity!!, SettingActivity::class.java)
        startActivity(intent)
    }

    private fun clearCache() {
        launch(Dispatchers.IO) {
            DBManager.deleteFiles(activity!!)
            FileUtil.clearAllCache(activity!!)
        }

        launch(Dispatchers.Main) {
            ToastUtil.showShort(activity!!, "清除成功")
        }

    }

    private fun aboutUs() {
        val packName = AppUtil.getPackageVersionName(activity!!, activity!!.packageName)
        val appName = getString(R.string.app_name)
        ToastUtil.show(activity!!, "$appName $packName")
    }


}