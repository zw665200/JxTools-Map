package com.recovery.tools.http.loader

import com.recovery.tools.bean.AlipayParam
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object AliPayLoader {

    fun getOrderParam(serviceId: Int): Observable<Response<AlipayParam>> {
        return RetrofitServiceManager.getInstance().aliPayParam.getOrderParam(
            serviceId,
            Constant.CLIENT_TOKEN,
            Constant.PRODUCT_ID,
            Constant.CHANNEL_ID
        )
    }
}