package com.recovery.tools

import android.app.Application
import com.baidu.lbsapi.BMapManager
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.baidu.mobads.action.BaiduAction
import com.bun.miitmdid.core.JLibrary
import com.hyphenate.chat.ChatClient
import com.hyphenate.helpdesk.easeui.UIProvider
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.utils.AppUtil
import com.recovery.tools.utils.JLog
import com.recovery.tools.utils.RomUtil
import com.tencent.bugly.Bugly
import com.tencent.mmkv.MMKV


class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initData()
        initRom()
        initHttpRequest()
        initBaiduMap()
        initMMKV()
        initBaiduAction()
        initIM()
        initBugly()
    }

    private fun initData() {
        if (AppUtil.isDebugger(this)) {
            Constant.isDebug = true
        }
    }

    /**
     * 读取设备信息
     *
     */
    private fun initRom() {
        val name = RomUtil.getName()
        JLog.i("name = $name")
        if (name != "") {
            Constant.ROM = name
        }
    }

    private fun initHttpRequest() {
        RetrofitServiceManager.getInstance().initRetrofitService()
    }

    private fun initBaiduMap() {
        SDKInitializer.initialize(this)
        SDKInitializer.setCoordType(CoordType.BD09LL)
        SDKInitializer.setHttpsEnable(true)
        val bMapManager = BMapManager(this)
        bMapManager.init { }
    }


    /**
     * init mmkv
     */
    private fun initMMKV() {
        MMKV.initialize(this)
    }

    private fun initBaiduAction() {
        //OPPO用户不激活OCPC
        if (!RomUtil.isOppo()) {
            JLibrary.InitEntry(this)
            BaiduAction.init(this, Constant.USER_ACTION_SET_ID, Constant.APP_SECRET_KEY)
            BaiduAction.setActivateInterval(this, 30)
            BaiduAction.setPrintLog(false)
        }
    }

    private fun initIM() {
        val option = ChatClient.Options()
        option.setAppkey(Constant.SDK_APP_KEY)
        option.setTenantId(Constant.SDK_APP_ID)

        if (!ChatClient.getInstance().init(this, option)) {
            return
        }

        UIProvider.getInstance().init(this)
    }

    private fun initBugly() {
        Bugly.init(applicationContext, Constant.BUGLY_APPID, false)
    }


}