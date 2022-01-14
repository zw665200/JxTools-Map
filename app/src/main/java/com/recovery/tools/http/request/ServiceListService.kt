package com.recovery.tools.http.request

import com.recovery.tools.bean.Price
import com.recovery.tools.controller.Constant
import com.recovery.tools.http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface ServiceListService {

    @GET("serverList/${Constant.PRODUCT_ID}")
    fun getServiceList(): Observable<Response<List<Price>>>
}