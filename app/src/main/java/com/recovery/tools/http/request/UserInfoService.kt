package com.recovery.tools.http.request

import com.recovery.tools.bean.UserInfo
import com.recovery.tools.http.response.Response
import io.reactivex.Observable
import retrofit2.http.*

interface UserInfoService {

    @POST("visit")
    @FormUrlEncoded
    fun getUser(@Field("questToken") token: String): Observable<Response<List<UserInfo>>>
}