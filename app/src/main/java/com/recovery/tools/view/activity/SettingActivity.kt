package com.recovery.tools.view.activity

import android.widget.ImageView
import android.widget.LinearLayout
import com.recovery.tools.R
import com.recovery.tools.controller.DBManager
import com.recovery.tools.utils.AppUtil
import com.recovery.tools.utils.FileUtil
import com.recovery.tools.utils.ToastUtil
import com.recovery.tools.view.base.BaseActivity
import kotlinx.coroutines.*

class SettingActivity : BaseActivity() {
    private lateinit var back: ImageView
    private lateinit var clear: LinearLayout
    private lateinit var about: LinearLayout


    override fun setLayout(): Int {
        return R.layout.a_setting
    }

    override fun initView() {
        back = findViewById(R.id.iv_back)
        back.setOnClickListener { finish() }

    }

    override fun initData() {


    }

    private fun clearCache() {
        launch(Dispatchers.IO) {
            DBManager.deleteFiles(this@SettingActivity)
            FileUtil.clearAllCache(this@SettingActivity)
        }

        launch(Dispatchers.Main) {
            ToastUtil.showShort(this@SettingActivity, "清除成功")
        }

    }

    private fun aboutUs() {
        val packName = AppUtil.getPackageVersionName(this, packageName)
        val appName = getString(R.string.app_name)
        ToastUtil.show(this, "$appName $packName")
    }
}