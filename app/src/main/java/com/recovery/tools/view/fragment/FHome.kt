package com.recovery.tools.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.baidu.location.BDLocation
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.recovery.tools.R
import com.recovery.tools.bean.Location
import com.recovery.tools.callback.LocationCallback
import com.recovery.tools.controller.MapManager
import com.recovery.tools.utils.ToastUtil
import com.recovery.tools.view.activity.BaiduPanoActivity
import com.recovery.tools.view.activity.PanoStreetActivity
import com.recovery.tools.view.activity.SearchActivity
import com.recovery.tools.view.base.BaseFragment

open class FHome : BaseFragment() {
    private lateinit var webView: WebView
    private lateinit var btn: ImageView
    private lateinit var bMap: MapView
    private lateinit var search: CardView
    private var location: Location? = null

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.f_home, container, false)
        webView = rootView.findViewById(R.id.webview)
        btn = rootView.findViewById(R.id.center_img)
        bMap = rootView.findViewById(R.id.baidu_map)
        search = rootView.findViewById(R.id.search)
        return rootView
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {
        Glide.with(this).load(R.mipmap.mapmark).into(btn)

        bMap.apply {
            showZoomControls(false)
            bMap.map.mapType = BaiduMap.MAP_TYPE_SATELLITE
            bMap.map.isMyLocationEnabled = true
            bMap.map.setMapStatus(MapStatusUpdateFactory.newMapStatus(MapStatus.Builder().zoom(6.0f).build()))
        }
        initMap()

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

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
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
            openPano()
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

                        this@FHome.location = Location(42.345573, -71.098326)

                        webView.evaluateJavascript("set(42.345573, -71.098326);", null)

                        val locData = MyLocationData.Builder()
                            .accuracy(location.radius)
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(location.direction).latitude(location.latitude)
                            .longitude(location.longitude).build()
                        bMap.map.setMyLocationData(locData)
                        bMap.map.setMapStatus(MapStatusUpdateFactory.newMapStatus(MapStatus.Builder().target(LatLng(latitude, longitude)).build()))

                    }
                }

                override fun failed(msg: String) {
                }
            })
        }
    }


    private fun openPano() {

        if (location != null) {
            val intent = Intent(activity, BaiduPanoActivity::class.java)
            intent.putExtra("location", location)
            startActivity(intent)
        }

    }

    override fun click(v: View?) {
    }

    override fun onResume() {
        super.onResume()
        bMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        bMap.onPause()
    }


    override fun onDestroy() {
        MapManager.get(activity!!).destroy()
        bMap.map.isMyLocationEnabled = false
        bMap.onDestroy()

        webView.apply {
            (parent as ViewGroup).removeAllViews()
            stopLoading()
            clearHistory()
            removeAllViewsInLayout()
            removeAllViews()
            destroy()
        }

        super.onDestroy()
    }

    inner class AndroidJavaScriptIp constructor(context: Context) {
        val mContext = context

        @JavascriptInterface
        fun showToast() {
            ToastUtil.showShort(mContext, "js show native")
        }

        @JavascriptInterface
        fun centerChanged(str: String) {
            val loc = Gson().fromJson(str, Location::class.java)
            if (loc != null) {
                location = loc
            }
            ToastUtil.showShort(mContext, loc.lat.toString())
        }


    }

}