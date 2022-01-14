package com.recovery.tools.view.activity

import com.baidu.lbsapi.panoramaview.PanoramaView
import com.recovery.tools.R
import com.recovery.tools.bean.Location
import com.recovery.tools.view.base.BaseActivity

class BaiduPanoActivity : BaseActivity() {
    private lateinit var mPanoView: PanoramaView
    private var location: Location? = null

    override fun setLayout(): Int {
        return R.layout.a_baidu_pano
    }


    override fun initView() {
        mPanoView = findViewById(R.id.panorama)
    }

    override fun initData() {
        location = intent.getParcelableExtra("location")
        mPanoView.setPanorama(location!!.lng, location!!.lng)
    }

    override fun onPause() {
        super.onPause()
        mPanoView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mPanoView.onResume()
    }

    override fun onDestroy() {
        mPanoView.destroy()
        super.onDestroy()
    }
}