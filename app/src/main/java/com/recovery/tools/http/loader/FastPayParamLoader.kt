package com.recovery.tools.http.loader

import com.recovery.tools.bean.FastPayParam
import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object FastPayParamLoader {

    fun getOrderParam(serviceId: Int): Observable<Response<FastPayParam>> {
        return RetrofitServiceManager.getInstance().fastPayParam.getOrderParam(
            serviceId,
            Constant.CLIENT_TOKEN,
            Constant.PRODUCT_ID,
            Constant.CHANNEL_ID
        )
    }
}