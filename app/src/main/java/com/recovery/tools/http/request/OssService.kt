package com.recovery.tools.http.request

import com.recovery.tools.bean.OssParam
import com.recovery.tools.http.response.Response
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OssService {

    @POST("grantAKToken")
    @FormUrlEncoded
    fun getOssToken(@Field("clientToken") clientToken: String): Observable<Response<OssParam>>
}