package com.recovery.tools.http.request

import com.recovery.tools.bean.Order
import com.recovery.tools.http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface OrderService {

    @POST("orderList")
    @FormUrlEncoded
    fun getOrders(@Field("clientToken") token: String): Observable<Response<List<Order>>>
}