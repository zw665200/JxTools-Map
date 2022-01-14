package com.recovery.tools.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.bumptech.glide.Glide
import com.recovery.tools.R
import com.recovery.tools.callback.LocationCallback
import com.recovery.tools.controller.MapManager
import com.recovery.tools.utils.JLog
import com.recovery.tools.utils.ToastUtil
import com.recovery.tools.view.base.BaseFragment

open class FVR : BaseFragment() {

    private lateinit var webView: WebView
    private lateinit var btn: Button

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.f_vr, container, false)
        webView = rootView.findViewById(R.id.webview)
        btn = rootView.findViewById(R.id.btn)
        return rootView
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {
        val setting = webView.settings
        setting.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url
                if (url != null) {
                    view?.loadUrl(url.toString())
                }
                return true
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return super.onJsAlert(view, url, message, result)
            }
        }

        webView.addJavascriptInterface(AndroidJavaScriptIp(activity!!), "native")
        webView.loadUrl("file:///android_asset/index.html")

        btn.setOnClickListener {
            initMap()
        }

    }

    private fun initMap() {
        checkPermissions {
            MapManager.get(activity!!).getLocation(object : LocationCallback {
                override fun success(location: BDLocation?) {
                    if (location != null) {
                        //获取纬度信息
                        val latitude = location.latitude
                        //获取经度信息
                        val longitude = location.longitude
//                        webView.evaluateJavascript("set($latitude, $longitude);", null)
                        webView.evaluateJavascript("set(42.345573, -71.098326);", null)
                    }
                }

                override fun failed(msg: String) {
                }
            })
        }
    }

    override fun click(v: View?) {
    }

    inner class AndroidJavaScriptIp constructor(context: Context) {
        val mContext = context

        @JavascriptInterface
        fun showToast() {
            ToastUtil.showShort(mContext, "js show native")
        }

    }

}