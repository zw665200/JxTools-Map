package com.recovery.tools.view.activity

import android.content.Intent
import android.os.*
import android.widget.ImageView
import android.widget.TextView
import com.recovery.tools.R
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.DBManager
import com.recovery.tools.http.loader.ConfigLoader
import com.recovery.tools.http.loader.ServiceListLoader
import com.recovery.tools.http.response.ResponseTransformer
import com.recovery.tools.http.schedulers.SchedulerProvider
import com.recovery.tools.utils.ToastUtil
import com.recovery.tools.view.base.BaseActivity
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.d_pics.*
import kotlinx.coroutines.*

/**
@author ZW
@description:
@date : 2020/11/25 10:31
 */
class SplashActivity : BaseActivity() {
    private lateinit var textView: TextView
    private lateinit var splashBg: ImageView
    private lateinit var timer: CountDownTimer
    private var kv = MMKV.defaultMMKV()

    override fun setLayout(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        textView = findViewById(R.id.splash_start)
        splashBg = findViewById(R.id.splash_bg)

        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }

        initTimer()
        clearDatabase()
        getConfig()
        getServiceList()
    }


    override fun initData() {
        val value = kv?.decodeBool("service_agree")
        if (value == null || !value) {
            val intent = Intent(this, AgreementActivity::class.java)
            startActivityForResult(intent, 0x1)
        } else {
            timer.start()
        }
    }

    private fun initTimer() {
        timer = object : CountDownTimer(2 * 1000L, 1000) {
            override fun onFinish() {
                jumpTo()
            }

            override fun onTick(millisUntilFinished: Long) {
//                textView.text = "${millisUntilFinished / 1000}"
            }
        }
    }


    private fun jumpTo() {
        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearDatabase() {
        launch(Dispatchers.IO) {
            DBManager.deleteFiles(this@SplashActivity)
        }
    }


    private fun getConfig() {
        launch {
            ConfigLoader.getConfig()
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe({
                    Constant.WEBSITE = it.offcialSite
                }, {
//                    ToastUtil.show(this@SplashActivity, "获取配置文件失败")
                })
        }
    }

    private fun getServiceList() {
        launch {
            ServiceListLoader.getServiceList()
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe({
                    if (it.isNotEmpty()) {
                        for (child in it) {
                            //save service list
                            MMKV.defaultMMKV()?.encode(child.server_code + child.expire_type, child)
                        }
                    }
                }, {
                    ToastUtil.show(this@SplashActivity, "获取服务列表失败")
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x1) {
            if (resultCode == 0x1) {
                kv?.encode("service_agree", true)
                timer.start()
            }

            if (resultCode == 0x2) {
                kv?.encode("service_agree", false)
                finish()
            }
        }
    }

}