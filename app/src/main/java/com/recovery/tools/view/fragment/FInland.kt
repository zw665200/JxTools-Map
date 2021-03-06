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
        list.add(Resource("website", R.drawable.tiananmen, "??????"))
        list.add(Resource("service", R.drawable.tiananmen, "??????"))
        list.add(Resource("privacy", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "?????????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "?????????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))

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
        list.add(Resource("website", R.drawable.tiananmen, "??????"))
        list.add(Resource("service", R.drawable.tiananmen, "??????"))
        list.add(Resource("privacy", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "?????????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "?????????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))
        list.add(Resource("feedback", R.drawable.tiananmen, "??????"))

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
            ToastUtil.showShort(activity!!, "????????????")
        }

    }

    private fun aboutUs() {
        val packName = AppUtil.getPackageVersionName(activity!!, activity!!.packageName)
        val appName = getString(R.string.app_name)
        ToastUtil.show(activity!!, "$appName $packName")
    }


}