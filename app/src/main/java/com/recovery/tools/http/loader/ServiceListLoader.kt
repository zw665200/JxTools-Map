package com.recovery.tools.http.loader

import com.recovery.tools.bean.Price
import com.recovery.tools.controller.RetrofitServiceManager
import com.recovery.tools.http.response.Response
import io.reactivex.Observable

object ServiceListLoader {

    fun getServiceList(): Observable<Response<List<Price>>> {
        return RetrofitServiceManager.getInstance().price.getServiceList()
    }
}