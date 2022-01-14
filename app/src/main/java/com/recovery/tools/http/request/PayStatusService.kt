package com.recovery.tools.http.request

import com.recovery.tools.bean.PayStatus
import com.recovery.tools.http.response.Response
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PayStatusService {

    @POST("serverStatus")
    @FormUrlEncoded
    fun getPayStatus(
        @Field("serverId") serverId: Int,
        @Field("clientToken") token: String
    ): Observable<Response<List<PayStatus>>>
}