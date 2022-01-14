package com.recovery.tools.http.loader

import com.recovery.tools.bean.WechatPayParam
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object WechatPayLoader {

    fun getOrderParam(serviceId: Int): Observable<Response<WechatPayParam>> {
        return RetrofitServiceManager.getInstance().wechatPayStatus.getOrderParam(
            serviceId,
            Constant.CLIENT_TOKEN,
            Constant.PRODUCT_ID,
            Constant.CHANNEL_ID
        )
    }
}