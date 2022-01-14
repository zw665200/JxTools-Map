package com.recovery.tools.callback

import com.baidu.location.BDLocation

/**
 * @author Herr_Z
 * @description:
 * @date : 2021/9/8 20:22
 */
interface LocationCallback {
    fun success(location: BDLocation?)
    fun failed(msg: String)
}