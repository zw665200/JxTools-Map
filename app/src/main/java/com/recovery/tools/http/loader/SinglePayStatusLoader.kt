package com.recovery.tools.http.loader

import com.recovery.tools.bean.PayStatus
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object SinglePayStatusLoader {

    fun getPayStatus(serviceId: Int, token: String): Observable<Response<List<PayStatus>>> {
        return RetrofitServiceManager.getInstance().singlePayStatus.getPayStatus(serviceId, token)
    }
}