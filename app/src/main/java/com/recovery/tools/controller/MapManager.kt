package com.recovery.tools.controller

import android.content.Context
import com.baidu.lbsapi.BMapManager
import com.baidu.lbsapi.MKGeneralListener
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.recovery.tools.callback.LocationCallback
import com.recovery.tools.utils.JLog

/**
 * @author Herr_Z
 * @description:
 * @date : 2021/9/8 20:10
 */
class MapManager private constructor(context: Context) {
    private var locationClient = LocationClient(context.applicationContext)

    companion object {

        @Volatile
        private var instance: MapManager? = null

        fun get(context: Context): MapManager {
            if (instance == null) {
                synchronized(WxManager::class) {
                    if (instance == null) {
                        instance = MapManager(context)
                    }
                }
            }
            return instance!!
        }
    }

    fun getLocation(callback: LocationCallback) {
        locationClient.registerLocationListener(object : BDAbstractLocationListener() {
            override fun onReceiveLocation(location: BDLocation?) {
                //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
                //以下只列举部分获取经纬度相关（常用）的结果信息
                //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

                //获取纬度信息
                val latitude = location?.latitude
                //获取经度信息
                val longitude = location?.longitude
                //获取定位精度，默认值为0.0f
                val radius = location?.radius
                //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
                val coorType = location?.coorType
                //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
                val errorCode = location?.locType

                if (errorCode == 61 || errorCode == 161) {
                    callback.success(location)
                } else {
                    callback.failed("error:$errorCode")
                }

                JLog.i("latitude = $latitude + longitude = $longitude")
                JLog.i("errorCode = $errorCode")
            }

            override fun onConnectHotSpotMessage(p0: String?, p1: Int) {
                super.onConnectHotSpotMessage(p0, p1)
            }

            override fun onLocDiagnosticMessage(p0: Int, p1: Int, p2: String?) {
                super.onLocDiagnosticMessage(p0, p1, p2)
            }

            override fun onReceiveVdrLocation(p0: BDLocation?) {
                super.onReceiveVdrLocation(p0)
            }

            override fun onReceiveLocString(p0: String?) {
                super.onReceiveLocString(p0)
            }
        })

        val locationOption = LocationClientOption()
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll
        locationOption.setCoorType("bd09ll")
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(0)
        //可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true)
        //可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true)
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false)
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.isLocationNotify = false
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true)
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true)
        //可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false)
        //可选，默认false，设置是否开启Gps定位
        locationOption.isOpenGps = true
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false)
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode()
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT)
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.locOption = locationOption
        //开始定位
        locationClient.start()
    }

    fun isInland(location: BDLocation): Boolean {
        var bl = true
        when (location.locationWhere) {
            BDLocation.LOCATION_WHERE_IN_CN -> bl = true
            BDLocation.LOCATION_WHERE_OUT_CN -> bl = false
            BDLocation.LOCATION_WHERE_UNKNOW -> bl = true
        }
        return bl
    }

    fun destroy() {
        locationClient.stop()
    }

}