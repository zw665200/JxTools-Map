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
import com.recovery.tools.view.activity.TutorialActivity
import com.recovery.tools.view.base.BaseFragment
import kotlinx.android.synthetic.main.item_function.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class FMine : BaseFragment() {
    private lateinit var title: TextView
    private lateinit var level: TextView
    private lateinit var phone: TextView
    private lateinit var customer: RecyclerView

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.f_mine, container, false)
        title = rootView.findViewById(R.id.tv_mine_nick)
        level = rootView.findViewById(R.id.tv_mine_vip)
        phone = rootView.findViewById(R.id.tv_mine_phone)
        customer = rootView.findViewById(R.id.function)
        return rootView
    }

    override fun initData() {
        val name = Constant.USER_NAME
        title.text = name
        loadFunction()
        loadDeviceInfo()
    }

    override fun click(v: View?) {
    }

    private fun loadFunction() {
        val list = arrayListOf<Resource>()
        list.add(Resource("website", R.drawable.mine_website, getString(R.string.mine_website)))
        list.add(Resource("service", R.drawable.mine_help, getString(R.string.mine_service)))
        list.add(Resource("privacy", R.drawable.mine_privacy, getString(R.string.mine_privacy)))
        list.add(Resource("feedback", R.drawable.mine_feedback, getString(R.string.mine_help)))
        list.add(Resource("clear", R.drawable.clear_cache, getString(R.string.setting_clear_cache)))
        list.add(Resource("about", R.drawable.about_us, getString(R.string.setting_about_us)))

        val mAdapter = DataAdapter.Builder<Resource>()
            .setData(list)
            .setLayoutId(R.layout.item_function)
            .addBindView { itemView, itemData, position ->
                Glide.with(activity!!).load(itemData.icon).into(itemView.function_icon)
                itemView.function_name.text = itemData.name

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

        customer.layoutManager = LinearLayoutManager(activity!!)
        customer.adapter = mAdapter
        mAdapter.notifyItemRangeChanged(0, list.size)
    }


    private fun loadDeviceInfo() {
        if (Build.BRAND == "HUAWEI" || Build.BRAND == "HONOR") {
            val name = Dict.getHUAWEIName(Build.MODEL)
            if (name.isNullOrEmpty()) {
                val b = "手机型号: ${Build.BRAND} ${Build.MODEL}"
                phone.text = b
            } else {
                val b = "手机型号: $name"
                phone.text = b
            }
        } else {
            val b = "手机型号: ${Build.BRAND} ${Build.MODEL}"
            phone.text = b
        }
    }


    private fun openWebsite() {
        if (Constant.WEBSITE == "") return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WEBSITE))
        startActivity(intent)
    }


    private fun openFeedback() {
        val intent = Intent(activity!!, CustomerServiceActivity::class.java)
        startActivity(intent)
    }

    private fun openUserAgreement() {
        val intent = Intent(activity!!, TutorialActivity::class.java)
        intent.putExtra("index", 0)
        startActivity(intent)
    }

    private fun openPrivacyAgreement() {
        val intent = Intent(activity!!, AgreementActivity::class.java)
        intent.putExtra("index", 1)
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