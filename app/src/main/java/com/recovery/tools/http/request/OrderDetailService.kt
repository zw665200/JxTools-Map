package com.recovery.tools.http.request

import com.recovery.tools.bean.OrderDetail
import com.recovery.tools.http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface OrderDetailService {

    @POST("orderDetail")
    @FormUrlEncoded
    fun getOrderDetail(@Field("orderSn") orderSn: String, @Field("clientToken") token: String): Observable<Response<OrderDetail>>
}