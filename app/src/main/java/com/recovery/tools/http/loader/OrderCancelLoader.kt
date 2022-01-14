package com.recovery.tools.http.loader

import com.recovery.tools.controller.Constant
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object OrderCancelLoader {

    fun orderCancel(orderSn: String): Observable<Response<String?>> {
        return RetrofitServiceManager.getInstance().orderCancel().orderCancel(orderSn, Constant.CLIENT_TOKEN)
    }
}